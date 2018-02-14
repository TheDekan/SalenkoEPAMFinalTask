<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html ng-app="price">
<head>
<title>Price control page</title>

<link rel="stylesheet" href="lib/dependencies/css/bootstrap.min.css" />
<link rel="stylesheet" href="lib/dependencies/css/ng-grid.min.css" />

<link rel="stylesheet" type="text/css" href="css/style4.css" />

<script src="lib/dependencies/angular.min.js"></script>
<script src="lib/dependencies/angular-resource.min.js"></script>
<script src="lib/dependencies/angular-route.min.js"></script>
<script src="lib/dependencies/jquery.min.js"></script>
<script src="lib/dependencies/ui-bootstrap-tpls.min.js"></script>
<script src="lib/dependencies/ng-grid.min.js"></script>

<script src="js/price_ru.js"></script>
</head>

<body>

	<div style="width: 100px; display:inline-block; float:left">
		<div id="wrapper">
			<ul class="menu2">
				<li><a href="PriceChange?action=start&locale=eng"><img src="https://purepetfood.co.uk/catalog/view/theme/pure/image/uk-small.png" style="height: 20px; width: 40px"/></a></li>
				<li><a href="PriceChange?action=start&locale=ru"><img src="https://upload.wikimedia.org/wikipedia/en/thumb/f/f3/Flag_of_Russia.svg/1200px-Flag_of_Russia.svg.png" style="height: 20px; width: 40px"/></a></li>
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

	<h1>Cтраница контроля цен</h1>

	<br />

	<div class="message" ng-controller="alertMessagesController">
		<alert ng-repeat="alert in alerts" type="{{alert.type}}"
			close="closeAlert($index)">{{alert.msg}}</alert>
	</div>

	<br>

	<div class="grid2" ng-controller="productsListController">
		<div>
			<h3>Список продуктов</h3>
		</div>

		<div class="gridStyle" ng-grid="gridOptions"></div>

		<pagination direction-links="true" boundary-links="true"
			total-items="price.totalResults" items-per-page="price.pageSize"
			ng-model="price.currentPage" ng-change="refreshGrid()">
		</pagination>
		<div>
			<form name="filterForm" ng-submit="sendFilter()" novalidate>

				<input type="hidden" name="thisIsFilter" ng-model="filter.thisIsFilter" ng-init="filter.thisIsFilter = true" />
				<div class="column">

					<div class="form-group"
						ng-class="{'has-error' : filterForm.fmodel.$invalid && filterForm.fmodel.$dirty}">
						<label for="fmodel">Модель:</label> <span
							ng-class="{'glyphicon glyphicon-ok' : filterForm.fmodel.$valid && filterForm.fmodel.$dirty}"></span>

						<input id="fmodel" name="fmodel" type="text" class="form-control"
							maxlength="30" ng-model="filter.fmodel" />
					</div>

					<div class="form-group"
						ng-class="{'has-error' : filterForm.fworthMin.$invalid && filterForm.fworthMin.$dirty}">
						<label for="fworthMin">ЦенаМин:</label> <span
							ng-class="{'glyphicon glyphicon-ok' : filterForm.fworthMin.$valid && filterForm.fworthMin.$dirty}"></span>

						<input id="fworthMin" name="fworthMin" type="number"
							class="form-control" ng-model="filter.fworthMin"
							ng-pattern="/^[0-9]+$/" step="1">

					</div>

					<div class="form-group"
						ng-class="{'has-error' : filterForm.fworthMax.$invalid && filterForm.fworthMax.$dirty}">
						<label for="fworthMax">ЦенаМакс:</label> <span
							ng-class="{'glyphicon glyphicon-ok' : filterForm.fworthMax.$valid && filterForm.fworthMax.$dirty}"></span>

						<input id="fworthMax" name="fworthMax" type="number"
							class="form-control" ng-model="filter.fworthMax"
							ng-pattern="/^[0-9]+$/" step="1">

					</div>

				</div>

				<div class="column">

					<div class="form-group"
						ng-class="{'has-error' : filterForm.fdealer.$invalid && filterForm.fdealer.$dirty}">
						<label for="fdealer">Диллер:</label> <span
							ng-class="{'glyphicon glyphicon-ok' : filterForm.fdealer.$valid && filterForm.fdealer.$dirty}"></span>

						<input id="fdealer" name="fdealer" type="text"
							class="form-control" maxlength="30" ng-model="filter.fdealer" />
					</div>

					<div class="form-group"
						ng-class="{'has-error' : filterForm.flengthMin.$invalid && filterForm.flengthMin.$dirty}">
						<label for="flengthMin">ДлинаМин:</label> <span
							ng-class="{'glyphicon glyphicon-ok' : filterForm.flengthMin.$valid && filterForm.flengthMin.$dirty}"></span>

						<input id="flengthMin" name="flengthMin" type="number"
							class="form-control" ng-model="filter.flengthMin"
							ng-pattern="/^[0-9]+$/" step="1">

					</div>

					<div class="form-group"
						ng-class="{'has-error' : filterForm.flengthMax.$invalid && filterForm.flengthMax.$dirty}">
						<label for="flengthMax">ДлинаМакс:</label> <span
							ng-class="{'glyphicon glyphicon-ok' : filterForm.flengthMax.$valid && filterForm.flengthMax.$dirty}"></span>

						<input id="flengthMax" name="flengthMax" type="number"
							class="form-control" ng-model="filter.flengthMax"
							ng-pattern="/^[0-9]+$/" step="1">

					</div>

				</div>

				<div class="column">

					<div class="form-group"
						ng-class="{'has-error' : filterForm.ftype.$invalid && filterForm.ftype.$dirty}">
						<label for="name">Тип:</label> <span
							ng-class="{'glyphicon glyphicon-ok' : filterForm.ftype.$valid && filterForm.ftype.$dirty}"></span>

						<select id="ftype" name="ftype" class="form-control"
							ng-model="filter.ftype">
							<option ng-selected="filter.ftype == ''" value="">Все</option>
							<option ng-selected="filter.ftype == 'fridge'" value="fridge">Холодильник</option>
							<option ng-selected="filter.ftype == 'stove'" value="stove">Печка</option>
							<option ng-selected="filter.ftype == 'washer'" value="washer">Стиралка</option>
						</select>

					</div>

					<div class="form-group"
						ng-class="{'has-error' : filterForm.fwidthMin.$invalid && filterForm.fwidthMin.$dirty}">
						<label for="fwidthMin">ШиринаМин:</label> <span
							ng-class="{'glyphicon glyphicon-ok' : filterForm.fwidthMin.$valid && filterForm.fwidthMin.$dirty}"></span>

						<input id="fwidthMin" name="fwidthMin" type="number"
							class="form-control" ng-model="filter.fwidthMin"
							ng-pattern="/^[0-9]+$/" step="1">

					</div>

					<div class="form-group"
						ng-class="{'has-error' : filterForm.fwidthMax.$invalid && filterForm.fwidthMax.$dirty}">
						<label for="fwidthMax">ШиринаМакс:</label> <span
							ng-class="{'glyphicon glyphicon-ok' : filterForm.fwidthMax.$valid && filterForm.fwidthMax.$dirty}"></span>

						<input id="fwidthMax" name="fwidthMax" type="number"
							class="form-control" ng-model="filter.fwidthMax"
							ng-pattern="/^[0-9]+$/" step="1">

					</div>

				</div>

				<div class="column">
					<div ng-if="filter.ftype == 'fridge'" class="form-group"
						ng-class="{'has-error' : filterForm.fspecialParameter2.$invalid && filterForm.fspecialParameter2.$dirty}">
						<label for="fspecialParameter2">СП&nbsp;2:</label> <span
							ng-class="{'glyphicon glyphicon-ok' : filterForm.fspecialParameter2.$valid && filterForm.fspecialParameter2.$dirty}"></span>

						<select id="fspecialParameter2" name="fspecialParameter2"
							class="form-control" ng-model="filter.fspecialParameter2">
							<option ng-selected="filter.fspecialParameter2 == ''" value="">Все</option>
							<option ng-selected="filter.fspecialParameter2 == 'no_frost'"
								value="no_frost">No&nbsp;frost</option>
							<option ng-selected="filter.fspecialParameter2 == 'drop_frost'"
								value="drop_frost">Капельный</option>
							<option ng-selected="filter.fspecialParameter2 == 'manual'"
								value="manual">Ручной</option>
						</select>

					</div>

					<div ng-if="filter.ftype != 'fridge'">
						<div ng-init="filter.fspecialParameter2 = null"></div>
					</div>


					<div ng-if="filter.ftype == 'stove'" class="form-group"
						ng-class="{'has-error' : filterForm.fspecialParameter2.$invalid && filterForm.fspecialParameter2.$dirty}">
						<label for="fspecialParameter2">СП&nbsp;2:</label> <span
							ng-class="{'glyphicon glyphicon-ok' : filterForm.fspecialParameter2.$valid && filterForm.fspecialParameter2.$dirty}"></span>

						<select id="fspecialParameter2" name="fspecialParameter2"
							class="form-control" ng-model="filter.fspecialParameter2">
							<option ng-selected="filter.fspecialParameter2 == ''" value="">Все</option>
							<option ng-selected="filter.fspecialParameter2 == 'electric'"
								value="electric">Електро</option>
							<option ng-selected="filter.fspecialParameter2 == 'gas'"
								value="gas">Газовая</option>
						</select>

					</div>

					<div ng-if="filter.ftype != 'stove'">
						<div ng-init="filter.fspecialParameter2 = null"></div>
					</div>

					<div ng-if="filter.ftype == 'washer'" class="form-group"
						ng-class="{'has-error' : filterForm.fspecialParameter2.$invalid && filterForm.fspecialParameter2.$dirty}">
						<label for="fspecialParameter2">СП&nbsp;2:</label> <span
							ng-class="{'glyphicon glyphicon-ok' : filterForm.fspecialParameter2.$valid && filterForm.fspecialParameter2.$dirty}"></span>

						<select id="fspecialParameter2" name="fspecialParameter2"
							class="form-control" ng-model="filter.fspecialParameter2">
							<option ng-selected="filter.fspecialParameter2 == ''" value="">Все</option>
							<option ng-selected="filter.fspecialParameter2 == 'vertical'"
								value="vertical">Вертикальная</option>
							<option ng-selected="filter.fspecialParameter2 == 'front'"
								value="front">Фронтальная</option>
						</select>

					</div>

					<div ng-if="filter.ftype != 'washer'">
						<div ng-init="filter.fspecialParameter2 = null"></div>
					</div>

					<div ng-if="filter.ftype == '' || filter.ftype == null"
						class="form-group"
						ng-class="{'has-error' : filterForm.specialParameter2.$invalid && filterForm.specialParameter2.$dirty}">
						<label for="fspecialParameter2">СП&nbsp;2:</label> <span
							ng-class="{'glyphicon glyphicon-ok' : filterForm.specialParameter2.$valid && filterForm.specialParameter2.$dirty}"></span>
						<select id="fspecialParameter2" name="fspecialParameter2"
							class="form-control" ng-model="filter.fspecialParameter2">
							<option ng-selected="filter.fspecialParameter2 == ''" value=""></option>
						</select>

					</div>

					<div class="form-group"
						ng-class="{'has-error' : filterForm.fheightMin.$invalid && filterForm.fheightMin.$dirty}">
						<label for="fheightMin">ВысотаМин:</label> <span
							ng-class="{'glyphicon glyphicon-ok' : filterForm.fheightMin.$valid && filterForm.fheightMin.$dirty}"></span>

						<input id="fheightMin" name="fheightMin" type="number"
							class="form-control" ng-model="filter.fheightMin"
							ng-pattern="/^[0-9]+$/" step="1">

					</div>

					<div class="form-group"
						ng-class="{'has-error' : filterForm.fheightMax.$invalid && filterForm.fheightMax.$dirty}">
						<label for="fheightMax">ВысотаМакс:</label> <span
							ng-class="{'glyphicon glyphicon-ok' : filterForm.fheightMax.$valid && filterForm.fheightMax.$dirty}"></span>

						<input id="fheightMax" name="fheightMax" type="number"
							class="form-control" ng-model="filter.fheightMax"
							ng-pattern="/^[0-9]+$/" step="1">

					</div>

				</div>

				<div style="display: inline-block">

					<div class="form-group" style="display: inline-block"
						ng-class="{'has-error' : filterForm.fspecialParameter1Min.$invalid && filterForm.fspecialParameter1Min.$dirty}">
						<label for="fspecialParameter1Min">СП1Мин:</label> <span
							ng-class="{'glyphicon glyphicon-ok' : filterForm.fspecialParameter1Min.$valid && filterForm.fspecialParameter1Min.$dirty}"></span>

						<input id="fspecialParameter1Min" name="fspecialParameter1Min"
							type="number" class="form-control"
							ng-model="filter.fspecialParameter1Min" ng-pattern="/^[0-9]+$/"
							step="1">

					</div>

					<div class="form-group" style="display: inline-block"
						ng-class="{'has-error' : filterForm.fspecialParameter1Max.$invalid && filterForm.fspecialParameter1Max.$dirty}">
						<label for="fspecialParameter1Max">СП1Макс:</label> <span
							ng-class="{'glyphicon glyphicon-ok' : filterForm.fspecialParameter1Max.$valid && filterForm.fspecialParameter1Max.$dirty}"></span>

						<input id="fspecialParameter1Max" name="fspecialParameter1Max"
							type="number" class="form-control"
							ng-model="filter.fspecialParameter1Max" ng-pattern="/^[0-9]+$/"
							step="1">

					</div>

				</div>

				<div class="buttons" style="display: inline-block">
					<button type="submit" class="btn btn-primary"
						ng-disabled="filterForm.$invalid">Фильтровать</button>
				</div>
			</form>
		</div>
	</div>

	<div class="form2" ng-controller="productFormController">
		<div ng-if="product.id == null">
			<h3>Добавить&nbsp;продукт</h3>
		</div>
		<div ng-if="product.id != null">
			<h3>Редактировать&nbsp;продукт</h3>
		</div>

		<div>
			<form name="productForm" ng-submit="updateProd()" novalidate>

				<div class="form-group"
					ng-class="{'has-error' : productForm.model.$invalid && productForm.model.$dirty}">
					<label for="model">Модель:</label> <span
						ng-class="{'glyphicon glyphicon-ok' : productForm.model.$valid && productForm.model.$dirty}"></span>

					<input id="model" name="model" type="text" class="form-control"
						maxlength="30" ng-model="product.model" required />

					<p class="help-block" style="display: inline"
						ng-show="productForm.model.$error.required">Введи&nbsp;модель.</p>
				</div>

				<div class="form-group"
					ng-class="{'has-error' : productForm.dealer.$invalid && productForm.dealer.$dirty}">
					<label for="dealer">Диллер:</label> <span
						ng-class="{'glyphicon glyphicon-ok' : productForm.dealer.$valid && productForm.dealer.$dirty}"></span>

					<input id="dealer" name="dealer" type="text" class="form-control"
						maxlength="30" ng-model="product.dealer" required />

					<p class="help-block" ng-show="productForm.dealer.$error.required">Введи&nbsp;диллера.</p>
				</div>

				<div class="form-group"
					ng-class="{'has-error' : productForm.type.$invalid && productForm.type.$dirty}">
					<label for="name">Тип:</label> <span
						ng-class="{'glyphicon glyphicon-ok' : productForm.type.$valid && productForm.type.$dirty}"></span>

					<select id="type" name="type" class="form-control"
						ng-model="product.type" required>
						<option ng-selected="product.type == 'fridge'" value="fridge">Холодильник</option>
						<option ng-selected="product.type == 'stove'" value="stove">Печка</option>
						<option ng-selected="product.type == 'washer'" value="washer">Стиралка</option>
					</select>

				</div>

				<div class="form-group"
					ng-class="{'has-error' : productForm.worth.$invalid && productForm.worth.$dirty}">
					<label for="worth">Цена:</label> <span
						ng-class="{'glyphicon glyphicon-ok' : productForm.worth.$valid && productForm.worth.$dirty}"></span>

					<input id="worth" name="worth" type="number" class="form-control"
						min="1" max="499999" ng-model="product.worth"
						ng-pattern="/^[0-9]+$/" step="1" required>

					<p class="help-block" ng-show="productForm.worth.$error.required">Введи&nbsp;цену.</p>
					<p class="help-block" ng-show="productForm.worth.$error.min">
						Минимум&nbsp;1.</p>
					<p class="help-block" ng-show="productForm.worth.$error.max">
						Максимум&nbsp;499999.</p>
				</div>

				<div class="form-group"
					ng-class="{'has-error' : productForm.length.$invalid && productForm.length.$dirty}">
					<label for="length">Длина:</label> <span
						ng-class="{'glyphicon glyphicon-ok' : productForm.length.$valid && productForm.length.$dirty}"></span>

					<input id="length" name="length" type="number" class="form-control"
						min="1" max="1000" ng-model="product.length"
						ng-pattern="/^[0-9]+$/" step="1" required>

					<p class="help-block" ng-show="productForm.length.$error.required">Введи&nbsp;длину.</p>
					<p class="help-block" ng-show="productForm.length.$error.min">Минимум&nbsp;1.</p>
					<p class="help-block" ng-show="productForm.length.$error.max">Максимум&nbsp;1000.</p>
				</div>

				<div class="form-group"
					ng-class="{'has-error' : productForm.width.$invalid && productForm.width.$dirty}">
					<label for="width">Ширина:</label> <span
						ng-class="{'glyphicon glyphicon-ok' : productForm.width.$valid && productForm.width.$dirty}"></span>

					<input id="width" name="width" type="number" class="form-control"
						min="1" max="1000" ng-model="product.width"
						ng-pattern="/^[0-9]+$/" step="1" required>

					<p class="help-block" ng-show="productForm.width.$error.required">Введи&nbsp;ширину.</p>
					<p class="help-block" ng-show="productForm.width.$error.min">Минимум&nbsp;1.</p>
					<p class="help-block" ng-show="productForm.width.$error.max">Максимум&nbsp;1000.</p>
				</div>

				<div class="form-group"
					ng-class="{'has-error' : productForm.height.$invalid && productForm.height.$dirty}">
					<label for="height">Высота:</label> <span
						ng-class="{'glyphicon glyphicon-ok' : productForm.height.$valid && productForm.height.$dirty}"></span>

					<input id="height" name="height" type="number" class="form-control"
						min="1" max="1000" ng-model="product.height"
						ng-pattern="/^[0-9]+$/" step="1" required>

					<p class="help-block" ng-show="productForm.height.$error.required">Введи&nbsp;высоту.</p>
					<p class="help-block" ng-show="productForm.height.$error.min">Минимум&nbsp;1.</p>
					<p class="help-block" ng-show="productForm.height.$error.max">Максимум&nbsp;1000.</p>
				</div>

				<div class="form-group"
					ng-class="{'has-error' : productForm.specialParameter1.$invalid && productForm.specialParameter1.$dirty}">
					<label for="specialParameter1">СП&nbsp;1:</label> <span
						ng-class="{'glyphicon glyphicon-ok' : productForm.specialParameter1.$valid && productForm.specialParameter1.$dirty}"></span>

					<input id="specialParameter1" name="specialParameter1"
						type="number" class="form-control" min="1" max="1000000"
						ng-model="product.specialParameter1" ng-pattern="/^[0-9]+$/"
						step="1" required>

					<p class="help-block"
						ng-show="productForm.specialParameter1.$error.required">Введи&nbsp;СП1.</p>
					<p class="help-block"
						ng-show="productForm.specialParameter1.$error.min">Минимум&nbsp;1.</p>
					<p class="help-block"
						ng-show="productForm.specialParameter1.$error.max">Максимум&nbsp;1000000.</p>
				</div>

				<div ng-if="product.type == 'fridge'" class="form-group"
					ng-class="{'has-error' : productForm.specialParameter2.$invalid && productForm.specialParameter2.$dirty}">
					<label for="specialParameter2">СП&nbsp;2:</label> <span
						ng-class="{'glyphicon glyphicon-ok' : productForm.specialParameter2.$valid && productForm.specialParameter2.$dirty}"></span>

					<select id="specialParameter2" name="specialParameter2"
						class="form-control" ng-model="product.specialParameter2"
						ng-required="product.type == 'fridge'">
						<option ng-selected="product.specialParameter2 == 'no_frost'"
							value="no_frost">No&nbsp;frost</option>
						<option ng-selected="product.specialParameter2 == 'drop_frost'"
							value="drop_frost">Капельный</option>
						<option ng-selected="product.specialParameter2 == 'manual'"
							value="manual">Ручной</option>
					</select>

				</div>

				<div ng-if="product.type != 'fridge'">
					<div ng-init="product.specialParameter2 = null"></div>
				</div>


				<div ng-if="product.type == 'stove'" class="form-group"
					ng-class="{'has-error' : productForm.specialParameter2.$invalid && productForm.specialParameter2.$dirty}">
					<label for="specialParameter2">СП&nbsp;2:</label> <span
						ng-class="{'glyphicon glyphicon-ok' : productForm.specialParameter2.$valid && productForm.specialParameter2.$dirty}"></span>

					<select id="specialParameter2" name="specialParameter2"
						class="form-control" ng-model="product.specialParameter2"
						ng-required="product.type == 'stove'">
						<option ng-selected="product.specialParameter2 == 'electric'"
							value="electric">Електро</option>
						<option ng-selected="product.specialParameter2 == 'gas'"
							value="gas">Газовая</option>
					</select>

				</div>

				<div ng-if="product.type != 'stove'">
					<div ng-init="product.specialParameter2 = null"></div>
				</div>

				<div ng-if="product.type == 'washer'" class="form-group"
					ng-class="{'has-error' : productForm.specialParameter2.$invalid && productForm.specialParameter2.$dirty}">
					<label for="specialParameter2">СП&nbsp;2:</label> <span
						ng-class="{'glyphicon glyphicon-ok' : productForm.specialParameter2.$valid && productForm.specialParameter2.$dirty}"></span>

					<select id="specialParameter2" name="specialParameter2"
						class="form-control" ng-model="product.specialParameter2"
						ng-required="product.type == 'washer'">
						<option ng-selected="product.specialParameter2 == 'vertical'"
							value="vertical">Вертикальная</option>
						<option ng-selected="product.specialParameter2 == 'front'"
							value="front">Горизонтальная</option>
					</select>

				</div>

				<div ng-if="product.type != 'washer'">
					<div ng-init="product.specialParameter2 = null"></div>
				</div>

				<div ng-if="product.type == null" class="form-group"
					ng-class="{'has-error' : productForm.specialParameter2.$invalid && productForm.specialParameter2.$dirty}">
					<label for="specialParameter2">СП&nbsp;2:</label> <span
						ng-class="{'glyphicon glyphicon-ok' : productForm.specialParameter2.$valid && productForm.specialParameter2.$dirty}"></span>

				</div>

				<div class="form-group"
					ng-class="{'has-error' : productForm.imageURL1.$invalid && productForm.imageURL1.$dirty}">
					<label for="imageURL1">URL1:</label> <span
						ng-class="{'glyphicon glyphicon-ok' : productForm.imageURL1.$valid && productForm.imageURL1.$dirty}"></span>

					<input id="imageURL1" name="imageURL1" type="text"
						class="form-control" maxlength="500" ng-model="product.imageURL1"
						required />

					<p class="help-block"
						ng-show="productForm.imageURL1.$error.required">Введи&nbsp;URL1.</p>
				</div>

				<div class="form-group"
					ng-class="{'has-error' : productForm.imageURL2.$invalid && productForm.imageURL2.$dirty}">
					<label for="imageURL2">URL2:</label> <span
						ng-class="{'glyphicon glyphicon-ok' : productForm.imageURL2.$valid && productForm.imageURL2.$dirty}"></span>

					<input id="imageURL2" name="imageURL2" type="text"
						class="form-control" maxlength="500" ng-model="product.imageURL2"
						required />

					<p class="help-block"
						ng-show="productForm.imageURL2.$error.required">Введи&nbsp;URL2.</p>
				</div>

				<div class="buttons">
					<button type="button" class="btn btn-primary"
						ng-click="clearForm()">Очистить</button>
					<button type="submit" class="btn btn-primary"
						ng-disabled="productForm.$invalid">Сохранить</button>
				</div>

				<div class="avatar" ng-if="product.imageURL1"
					style="display: inline-block">
					<img ng-src="{{product.imageURL1}}"
						style="max-height: 260px; max-width: 100px" />
				</div>
				<div class="avatar" ng-if="product.imageURL2"
					style="display: inline-block">
					<img ng-src="{{product.imageURL2}}"
						style="max-height: 260px; max-width: 100px" />
				</div>

			</form>
		</div>
	</div>

</body>
</html>