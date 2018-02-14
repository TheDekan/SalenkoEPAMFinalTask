<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html ng-app="cart">
<head>
<title>Cart control page</title>

<link rel="stylesheet" href="lib/dependencies/css/bootstrap.min.css" />
<link rel="stylesheet" href="lib/dependencies/css/ng-grid.min.css" />

<link rel="stylesheet" type="text/css" href="css/style4.css" />

<script src="lib/dependencies/angular.min.js"></script>
<script src="lib/dependencies/angular-resource.min.js"></script>
<script src="lib/dependencies/angular-route.min.js"></script>
<script src="lib/dependencies/jquery.min.js"></script>
<script src="lib/dependencies/ui-bootstrap-tpls.min.js"></script>
<script src="lib/dependencies/ng-grid.min.js"></script>

<script src="js/cart_ru.js"></script>
</head>

<body>

	<div style="width: 100px; display:inline-block; float:left">
		<div id="wrapper">
			<ul class="menu2">
				<li><a href="Cart?action=start&locale=eng"><img src="https://purepetfood.co.uk/catalog/view/theme/pure/image/uk-small.png" style="height: 20px; width: 40px"/></a></li>
				<li><a href="Cart?action=start&locale=ru"><img src="https://upload.wikimedia.org/wikipedia/en/thumb/f/f3/Flag_of_Russia.svg/1200px-Flag_of_Russia.svg.png" style="height: 20px; width: 40px"/></a></li>
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

	<h1>Корзина</h1>

	<br />

	<div class="message" ng-controller="alertMessagesController">
		<alert ng-repeat="alert in alerts" type="{{alert.type}}"
			close="closeAlert($index)">{{alert.msg}}</alert>
	</div>

	<br>

	<div class="grid2" ng-controller="cartsListController">
		<div>
			<h3>Список покупок</h3>
		</div>

		<div class="gridStyle" ng-grid="gridOptions"></div>

		<pagination direction-links="true" boundary-links="true"
			total-items="cart.totalResults" items-per-page="cart.pageSize"
			ng-model="cart.currentPage" ng-change="refreshGrid()"> </pagination>
		<div>
			<form name="dealForm" ng-submit="sendDeal()" novalidate>

				<input type="hidden" name="doDeal" ng-model="deal.doDeal" ng-init="deal.doDeal = true" />

				<div class="buttons" style="display: inline-block">
					<button type="submit" class="btn btn-primary"
						ng-disabled="dealForm.$invalid">Подтвердить</button>
				</div>
			</form>
			Обратите внимание. После нажатия клавиши-сделки вы подтвердите сделку и более не сможете её изменить, кроме как отказавшись. 
			Упаковка и отсылка выших товаров начнется сразу после внесения оплаты и подтверждения администратором.
		</div>
	</div>

	<div class="form2" ng-controller="cartFormController">
		<div ng-if="cart.id == null">
			<h3>Чек</h3>
			<table border="1" cellpadding="5">
				<tr>
					<th>Продукт</th>
					<th>Количество</th>
					<th>Цена</th>
				</tr>
				<tr ng-repeat="cart in cartList track by cart.id">
					<td>{{cart.productRow}}</td>
					<td>{{cart.count}}</td>
					<td>{{cart.totalWorth}}</td>
				</tr>
				<td>Итого:</td>
				<td></td>
				<td>{{check}}</td>
			</table>


		</div>
		<div ng-if="cart.id != null">
			<h3>Данные продукта</h3>
		</div>

		<div>
			<form ng-if="cart.id != null" name="cartForm"
				ng-submit="updateCartItem()" novalidate>

				<div class="form-group"
					ng-class="{'has-error' : cartForm.productRow.$invalid && cartForm.productRow.$dirty}">
					<label for="productRow">Продукт:</label> <span
						ng-class="{'glyphicon glyphicon-ok' : cartForm.productRow.$valid && cartForm.productRow.$dirty}"></span>

					<label class="valueLabel">{{cart.productRow}}</label>

				</div>

				<div class="form-group"
					ng-class="{'has-error' : cartForm.worth.$invalid && cartForm.worth.$dirty}">
					<label for="worth">Итого:</label> <span
						ng-class="{'glyphicon glyphicon-ok' : cartForm.worth.$valid && cartForm.worth.$dirty}"></span>

					<label class="valueLabel">{{cart.totalWorth}}</label>

				</div>

				<div class="form-group"
					ng-class="{'has-error' : cartForm.count.$invalid && cartForm.count.$dirty}">
					<label for="count">Кол-во:</label> <span
						ng-class="{'glyphicon glyphicon-ok' : cartForm.count.$valid && cartForm.count.$dirty}"></span>

					<input id="count" name="count" type="number" class="form-control"
						ng-model="cart.count" ng-pattern="/^[0-9]+$/" step="1">

				</div>

				<div class="buttons">
					<button type="button" class="btn btn-primary"
						ng-click="clearForm()">Отменить</button>
					<button type="submit" class="btn btn-primary"
						ng-disabled="cartForm.$invalid">Сохранить</button>
				</div>

			</form>
		</div>
	</div>

</body>
</html>