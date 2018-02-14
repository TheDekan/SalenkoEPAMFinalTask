var app = angular.module('user', ['ngResource', 'ngGrid', 'ui.bootstrap']);

app.controller('usersListController', function ($scope, $rootScope, userService) {
    
    $scope.sortInfo = { fields: ['id'], directions: ['asc']};
    $scope.user = {currentPage: 1};

    $scope.gridOptions = {
        data: 'user.list',
        useExternalSorting: true,

        columnDefs: [
            { field: 'name', displayName: 'Email' },
            { field: 'role', displayName: 'Role' },
            { field: 'joinDate', displayName: 'Join date' },
            { field: 'blocked', displayName: 'Block' }          
        ],

        multiSelect: false,
        selectedItems: [],
        
        afterSelectionChange: function (rowItem) {
            if (rowItem.selected) {
                $rootScope.$broadcast('userSelected', $scope.gridOptions.selectedItems[0].id);
            }
        }
    };

    $scope.refreshGrid = function () {
    	var listUsersArgs = {
                action: 'list',
                page: $scope.user.currentPage,
                sortFields: $scope.sortInfo.fields[0],
                sortDirections: $scope.sortInfo.directions[0],
                fname:$scope.filter.fname,
                thisIsFilter:$scope.filter.thisIsFilter
        };

        userService.get(listUsersArgs, function (data) {
            $scope.user = data;
        })
    };

    $scope.deleteRow = function (row) {
        $rootScope.$broadcast('deleteUser', row.entity.id);
    };

    $scope.$watch('sortInfo', function () {
        $scope.user = {currentPage: 1};
            $scope.refreshGrid();
    }, true);

    $scope.$on('ngGridEventSorted', function (event, sortInfo) {
        $scope.sortInfo = sortInfo;
    });

        $scope.$on('refreshGrid', function () {
        $scope.refreshGrid();
    });

        $scope.$on('clear', function () {
        $scope.gridOptions.selectAll(false);
    });
        
});

app.controller('userFormController', function ($scope, $rootScope, userService) {
    
      $scope.userList = [];
      $scope.check = null;
      
      userService.get(function (data) {
          $scope.userItemList = data.list;
      })
     
        $scope.clearForm = function () {
        $scope.user = null;
        $scope.userForm.$setPristine();
        $rootScope.$broadcast('clear');
    };

        $scope.updateUserItem = function () {
        userService.save($scope.user).$promise.then(
            function () {
                $rootScope.$broadcast('refreshGrid');
                $rootScope.$broadcast('userSaved');
                $scope.clearForm();
            },
            function () {
                $rootScope.$broadcast('error');
            });
    };

        $scope.$on('userSelected', function (event, id) {
        $scope.user = userService.get({id: id});
    });

        $scope.$on('deleteUser', function (event, id) {
        userService.delete({id: id}).$promise.then(
            function () {
                $rootScope.$broadcast('refreshGrid');
                $rootScope.$broadcast('userDeleted');
                $scope.clearForm();
            },
            function () {
                $rootScope.$broadcast('error');
            });
    });
});

app.controller('alertMessagesController', function ($scope) {
        $scope.$on('userSaved', function () {
        $scope.alerts = [
            { type: 'success', msg: 'User row saved successfully!' }
        ];
    });

        $scope.$on('error', function () {
        $scope.alerts = [
            { type: 'danger', msg: 'There was a problem in the server!' }
        ];
    });

    $scope.closeAlert = function (index) {
        $scope.alerts.splice(index, 1);
    };
});

app.factory('userService', function ($resource) {
    return $resource('User?action=list&userId=:id');
});
