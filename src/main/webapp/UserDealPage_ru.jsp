<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html ng-app="deal">
<head>
<title>Deal control page</title>

<link rel="stylesheet" href="lib/dependencies/css/bootstrap.min.css" />
<link rel="stylesheet" href="lib/dependencies/css/ng-grid.min.css" />

<link rel="stylesheet" type="text/css" href="css/style4.css" />

<script src="lib/dependencies/angular.min.js"></script>
<script src="lib/dependencies/angular-resource.min.js"></script>
<script src="lib/dependencies/angular-route.min.js"></script>
<script src="lib/dependencies/jquery.min.js"></script>
<script src="lib/dependencies/ui-bootstrap-tpls.min.js"></script>
<script src="lib/dependencies/ng-grid.min.js"></script>

<script src="js/deal_ru.js"></script>
</head>

<body>

	<div style="width: 100px; display:inline-block; float:left">
		<div id="wrapper">
			<ul class="menu2">
				<li><a href="Deal?action=myDeals&locale=eng"><img src="https://purepetfood.co.uk/catalog/view/theme/pure/image/uk-small.png" style="height: 20px; width: 40px"/></a></li>
				<li><a href="Deal?action=myDeals&locale=ru"><img src="https://upload.wikimedia.org/wikipedia/en/thumb/f/f3/Flag_of_Russia.svg/1200px-Flag_of_Russia.svg.png" style="height: 20px; width: 40px"/></a></li>
			</ul>
		</div>
	</div>

	<div id="wrapper">
		<ul class="menu">
			<li><a href="${pageContext.request.contextPath}">Старт</a></li>
			<li><a href="Fridge?action=start">Холодильники</a></li>
			<li><a href="Stove?action=start">Печи</a></li>
			<li><a href="Washer?action=start">Стиралки</a></li>
			<li	ng-if="'${sessionScope.role}' == 'admin' || '${sessionScope.role}' == 'Customer'"><a href="Cart?action=start">Корзина</a></li>
			<li	ng-if="'${sessionScope.role}' == 'admin' || '${sessionScope.role}' == 'Customer'"><a href="Deal?action=myDeals">Сделки</a></li>
			<li ng-if="'${sessionScope.role}' == 'admin'"><a href="Deal?action=deals">Все сделки</a></li>
			<li ng-if="'${sessionScope.role}' == 'admin'"><a href="PriceChange?action=start">Прайс</a></li>
			<li ng-if="'${sessionScope.role}' == 'admin'"><a href="User?action=start">Юзеры</a></li>
			<li ng-if="'${sessionScope.role}' == ''"><a href="AuthorizationController">Логин</a></li>
			<li	ng-if="'${sessionScope.role}' == 'admin' || '${sessionScope.role}' == 'Customer'"><a href="AuthorizationController?logout">Логаут</a></li>
		</ul>
	</div>

	<h1>Мои сделки</h1>

	<br />

	<div class="message" ng-controller="alertMessagesController">
		<alert ng-repeat="alert in alerts" type="{{alert.type}}"
			close="closeAlert($index)">{{alert.msg}}</alert>
	</div>

	<br>

	<div class="grid2" ng-controller="dealsListController">
		<div>
			<h3>Список сделок</h3>
		</div>

		<div class="gridStyle" ng-grid="gridOptions"></div>

		<pagination direction-links="true" boundary-links="true"
			total-items="deal.totalResults" items-per-page="deal.pageSize"
			ng-model="deal.currentPage" ng-change="refreshGrid()"> </pagination>
			
		<div>
			<form name="filterForm" ng-submit="sendFilter()" novalidate>

				<input type="hidden" name="thisIsFilter" ng-model="filter.thisIsFilter" ng-init="filter.thisIsFilter = true" />
				
				<div class="form-group"
					ng-class="{'has-error' : filterForm.fproduct.$invalid && filterForm.fproduct.$dirty}">
					<label for="fproduct">Продукт:</label> <span
						ng-class="{'glyphicon glyphicon-ok' : filterForm.fproduct.$valid && filterForm.fproduct.$dirty}"></span>

					<input id="fproduct" name="fproduct" type="text" class="form-control"
						maxlength="30" ng-model="filter.fproduct" />
				</div>

				<div class="buttons" style="display: inline-block">
					<button type="submit" class="btn btn-primary"
						ng-disabled="filterForm.$invalid">Фильтровать</button>
				</div>
			</form>
		</div>
		
	</div>

	<div class="form2" ng-controller="dealFormController">
		<div ng-if="deal.id == null">
			<h3>Выберите сделку</h3>
		</div>
		<div ng-if="deal.id != null">
			<h3>Данные сделки</h3>
		</div>

		<div>
			<form ng-if="deal.id != null" name="dealForm"
				ng-submit="updateDealItem()" novalidate>

				<table border="1" cellpadding="5">
					<tr>
						<th>Продукт</th>
						<th>Количество</th>
						<th>Цена</th>
					</tr>
					<tr ng-repeat="deal in deal.list track by deal.id">
						<td>{{deal.productRow}}</td>
						<td>{{deal.productCount}}</td>
						<td>{{deal.worth}}</td>
					</tr>
				</table>

				<div class="buttons">
					<button type="button" class="btn btn-primary"
						ng-click="clearForm()">Очистить</button>
				</div>

			</form>
		</div>
	</div>

</body>
</html>