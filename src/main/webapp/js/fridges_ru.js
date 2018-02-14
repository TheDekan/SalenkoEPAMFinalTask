var app = angular.module('fridges', ['ngResource', 'ngGrid', 'ui.bootstrap']);

app.controller('fridgesListController', function ($scope, $rootScope, productService) {
    
    $scope.sortInfo = { fields: ['id'], directions: ['asc']};
    $scope.fridges = {currentPage: 1};

    $scope.gridOptions = {
        data: 'fridges.list',
        useExternalSorting: true,

        columnDefs: [
            { field: 'dealer', displayName: 'Диллер' },
            { field: 'model', displayName: 'Модель' },
            { field: 'worth', displayName: 'Цена' },
            { field: 'length', displayName: 'Длина' },
            { field: 'width', displayName: 'Ширина' },
            { field: 'height', displayName: 'Высота' },
            { field: 'specialParameter1', displayName: 'Объем' },
            { field: 'specialParameter2', displayName: 'Тип' }         
        ],

        multiSelect: false,
        selectedItems: [],
        
        afterSelectionChange: function (rowItem) {
            if (rowItem.selected) {
                $rootScope.$broadcast('productSelected', $scope.gridOptions.selectedItems[0].id);
            }
        }
    };

    $scope.refreshGrid = function () {
        var listProductsArgs = {
            action: 'list',
            page: $scope.fridges.currentPage,
            sortFields: $scope.sortInfo.fields[0],
            sortDirections: $scope.sortInfo.directions[0],
            fmodel:$scope.filter.fmodel,
            fdealer:$scope.filter.fdealer,
            ftype:$scope.filter.ftype,
            fspecialParameter2:$scope.filter.fspecialParameter2,
            fworthMin:$scope.filter.fworthMin,
            fworthMax:$scope.filter.fworthMax,
            flengthMin:$scope.filter.flengthMin,
            flengthMax:$scope.filter.flengthMax,
            fwidthMin:$scope.filter.fwidthMin,
            fwidthMax:$scope.filter.fwidthMax,
            fheightMin:$scope.filter.fheightMin,
            fheightMax:$scope.filter.fheightMax,
            fspecialParameter1Min:$scope.filter.fspecialParameter1Min,
            fspecialParameter1Max:$scope.filter.fspecialParameter1Max,
            thisIsFilter:$scope.filter.thisIsFilter
        };

        productService.get(listProductsArgs, function (data) {
            $scope.fridges = data;
        })
    };

    $scope.deleteRow = function (row) {
        $rootScope.$broadcast('deleteProduct', row.entity.id);
    };

    $scope.$watch('sortInfo', function () {
        $scope.fridges = {currentPage: 1};
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
            productService.save($scope.filter).$promise.then(
                function () {
                    $rootScope.$broadcast('refreshGrid');
                },
                function () {
                    $rootScope.$broadcast('error');
                });
        };
});

app.controller('fridgeFormController', function ($scope, $rootScope, productService, $location, $window) {
    
      $scope.productList = [];
      
      productService.get(function (data) {
          $scope.productList = data.productList; 
      })
     
        $scope.clearForm = function () {
        $scope.product = null;
        $scope.productForm.$setPristine();
        $rootScope.$broadcast('clear');
    };

    $scope.addToCart = function () {
        
        productService.save($scope.product).$promise.then(
            function () {
            	var role = document.getElementById('RoleValue').value;
            	if (role === "" || role === null) {
            		$window.location.href = "AuthorizationController";
            	} else {
            		$rootScope.$broadcast('refreshGrid');
                    $rootScope.$broadcast('productSaved');
                    $scope.clearForm();
            	}               
            },
            function (response) {
                $rootScope.$broadcast('error');
            });
    };

        $scope.$on('productSelected', function (event, id) {
        $scope.product = productService.get({id: id});
    });

        $scope.$on('deleteProduct', function (event, id) {
        productService.delete({id: id}).$promise.then(
            function () {
                $rootScope.$broadcast('refreshGrid');
                $rootScope.$broadcast('productDeleted');
                $scope.clearForm();
            },
            function () {
                $rootScope.$broadcast('error');
            });
    });
});

app.controller('alertMessagesController', function ($scope) {
        $scope.$on('productSaved', function () {
        $scope.alerts = [
            { type: 'success', msg: 'Продукт добавлен успешно!' }
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

app.factory('productService', function ($resource) {
    return $resource('Fridge?action=list&productId=:id');
});
