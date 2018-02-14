var app = angular.module('cart', ['ngResource', 'ngGrid', 'ui.bootstrap']);

app.controller('cartsListController', function ($scope, $rootScope, cartService) {
    
    $scope.sortInfo = { fields: ['id'], directions: ['asc']};
    $scope.cart = {currentPage: 1};

    $scope.gridOptions = {
        data: 'cart.list',
        useExternalSorting: true,

        columnDefs: [
            { field: 'productRow', displayName: 'Продукт' },
            { field: 'count', width: 80, displayName: 'Количество' },
            { field: 'worthPerItem', displayName: 'Цена за 1' },
            { field: '', width: 30, cellTemplate: '<span class="glyphicon glyphicon-remove remove" ng-click="deleteRow(row)"></span>' }          
        ],

        multiSelect: false,
        selectedItems: [],
        
        afterSelectionChange: function (rowItem) {
            if (rowItem.selected) {
                $rootScope.$broadcast('cartSelected', $scope.gridOptions.selectedItems[0].id);
            }
        }
    };

    $scope.refreshGrid = function () {
        var listCartsArgs = {
            action: 'list',
            page: $scope.cart.currentPage,
            sortFields: $scope.sortInfo.fields[0],
            sortDirections: $scope.sortInfo.directions[0]
        };

        cartService.get(listCartsArgs, function (data) {
            $scope.cart = data;
        })
    };

    $scope.deleteRow = function (row) {
        $rootScope.$broadcast('deleteCart', row.entity.id);
    };

    $scope.$watch('sortInfo', function () {
        $scope.cart = {currentPage: 1};
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
        
        $scope.sendDeal = function () {
            cartService.save($scope.deal).$promise.then(
                function () {
                    $rootScope.$broadcast('refreshGrid');
                },
                function () {
                    $rootScope.$broadcast('error');
                });
        };
});

app.controller('cartFormController', function ($scope, $rootScope, cartService) {
    
      $scope.cartList = [];
      $scope.check = null;
      
      cartService.get(function (data) {
          $scope.cartList = data.list;
          $scope.check = data.check;
      })
     
        $scope.clearForm = function () {
        $scope.cart = null;
        $scope.cartForm.$setPristine();
        $rootScope.$broadcast('clear');
    };

        $scope.updateCartItem = function () {
        cartService.save($scope.cart).$promise.then(
            function () {
                $rootScope.$broadcast('refreshGrid');
                $rootScope.$broadcast('cartSaved');
                $scope.clearForm();
            },
            function () {
                $rootScope.$broadcast('error');
            });
    };

        $scope.$on('cartSelected', function (event, id) {
        $scope.cart = cartService.get({id: id});
    });

        $scope.$on('deleteCart', function (event, id) {
        cartService.delete({id: id}).$promise.then(
            function () {
                $rootScope.$broadcast('refreshGrid');
                $rootScope.$broadcast('cartDeleted');
                $scope.clearForm();
            },
            function () {
                $rootScope.$broadcast('error');
            });
    });
});

app.controller('alertMessagesController', function ($scope) {
        $scope.$on('cartSaved', function () {
        $scope.alerts = [
            { type: 'success', msg: 'Изменения внесены успешно!' }
        ];
    });

        $scope.$on('cartDeleted', function () {
        $scope.alerts = [
            { type: 'success', msg: 'Строка удалена успешно!' }
        ];
    });

        $scope.$on('error', function () {
        $scope.alerts = [
            { type: 'danger', msg: 'Проблемы на сервере!' }
        ];
    });

    $scope.closeAlert = function (index) {
        $scope.alerts.splice(index, 1);
    };
});

app.factory('cartService', function ($resource) {
    return $resource('Cart?action=list&cartId=:id');
});
