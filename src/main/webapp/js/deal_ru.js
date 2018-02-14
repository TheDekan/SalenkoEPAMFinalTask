var app = angular.module('deal', ['ngResource', 'ngGrid', 'ui.bootstrap']);

app.controller('dealsListController', function ($scope, $rootScope, dealService) {
    
    $scope.sortInfo = { fields: ['id'], directions: ['asc']};
    $scope.deal = {currentPage: 1};

    $scope.gridOptions = {
        data: 'deal.list',
        useExternalSorting: true,

        columnDefs: [
            { field: 'worth', displayName: 'Цена' },
            { field: 'sendDate', displayName: 'Дата запроса' },
            { field: 'status', displayName: 'Статус' }          
        ],

        multiSelect: false,
        selectedItems: [],
        
        afterSelectionChange: function (rowItem) {
            if (rowItem.selected) {
                $rootScope.$broadcast('dealSelected', $scope.gridOptions.selectedItems[0].id);
            }
        }
    };

    $scope.refreshGrid = function () {
        var listDealsArgs = {
            action: 'list',
            page: $scope.deal.currentPage,
            sortFields: $scope.sortInfo.fields[0],
            sortDirections: $scope.sortInfo.directions[0],
            fproduct:$scope.filter.fproduct,
            thisIsFilter:$scope.filter.thisIsFilter
        };

        dealService.get(listDealsArgs, function (data) {
            $scope.deal = data;
        })
    };

    $scope.deleteRow = function (row) {
        $rootScope.$broadcast('deleteDeal', row.entity.id);
    };

    $scope.$watch('sortInfo', function () {
        $scope.deal = {currentPage: 1};
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

        $scope.sendFilter = function () {
            dealService.save($scope.filter).$promise.then(
                function () {
                    $rootScope.$broadcast('refreshGrid');
                },
                function () {
                    $rootScope.$broadcast('error');
                });
        };
        
});

app.controller('dealFormController', function ($scope, $rootScope, dealService) {
    
      $scope.dealList = [];
      $scope.check = null;
      
      dealService.get(function (data) {
          $scope.dealItemList = data.list;
      })
     
        $scope.clearForm = function () {
        $scope.deal = null;
        $scope.dealForm.$setPristine();
        $rootScope.$broadcast('clear');
    };

        $scope.updateDealItem = function () {
        dealService.save($scope.deal).$promise.then(
            function () {
                $rootScope.$broadcast('refreshGrid');
                $rootScope.$broadcast('dealSaved');
                $scope.clearForm();
            },
            function () {
                $rootScope.$broadcast('error');
            });
    };

        $scope.$on('dealSelected', function (event, id) {
        $scope.deal = dealService.get({id: id});
    });

        $scope.$on('deleteDeal', function (event, id) {
        dealService.delete({id: id}).$promise.then(
            function () {
                $rootScope.$broadcast('refreshGrid');
                $rootScope.$broadcast('dealDeleted');
                $scope.clearForm();
            },
            function () {
                $rootScope.$broadcast('error');
            });
    });
});

app.controller('alertMessagesController', function ($scope) {

        $scope.$on('error', function () {
        $scope.alerts = [
            { type: 'danger', msg: 'Проблемы на сервере!' }
        ];
    });

    $scope.closeAlert = function (index) {
        $scope.alerts.splice(index, 1);
    };
});

app.factory('dealService', function ($resource) {
    return $resource('Deal?action=list&dealId=:id');
});
