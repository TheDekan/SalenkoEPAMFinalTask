<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html ng-app="fridges">
<head>
<title>Fridges</title>

<link rel="stylesheet" href="lib/dependencies/css/bootstrap.min.css" />
<link rel="stylesheet" href="lib/dependencies/css/ng-grid.min.css" />

<link rel="stylesheet" type="text/css" href="css/style4.css" />

<script src="lib/dependencies/angular.min.js"></script>
<script src="lib/dependencies/angular-resource.min.js"></script>
<script src="lib/dependencies/angular-route.min.js"></script>
<script src="lib/dependencies/jquery.min.js"></script>
<script src="lib/dependencies/ui-bootstrap-tpls.min.js"></script>
<script src="lib/dependencies/ng-grid.min.js"></script>

<script src="js/fridges.js"></script>
</head>

<body>

	<input id="RoleValue" type="hidden" value="${sessionScope.role}" />
	
	<div style="width: 100px; display:inline-block; float:left">
		<div id="wrapper">
			<ul class="menu2">
				<li><a href="Fridge?action=start&locale=eng"><img src="https://purepetfood.co.uk/catalog/view/theme/pure/image/uk-small.png" style="height: 20px; width: 40px"/></a></li>
				<li><a href="Fridge?action=start&locale=ru"><img src="https://upload.wikimedia.org/wikipedia/en/thumb/f/f3/Flag_of_Russia.svg/1200px-Flag_of_Russia.svg.png" style="height: 20px; width: 40px"/></a></li>
			</ul>
		</div>
	</div>

	<div id="wrapper">
		<ul class="menu">
			<li><a href="${pageContext.request.contextPath}">Start</a></li>
			<li><a href="Fridge?action=start">Fridges</a></li>
			<li><a href="Stove?action=start">Stoves</a></li>
			<li><a href="Washer?action=start">Washers</a></li>
			<li	ng-if="'${sessionScope.role}' == 'admin' || '${sessionScope.role}' == 'Customer'"><a href="Cart?action=start">Cart</a></li>
			<li	ng-if="'${sessionScope.role}' == 'admin' || '${sessionScope.role}' == 'Customer'"><a href="Deal?action=myDeals">Deals</a></li>
			<li ng-if="'${sessionScope.role}' == 'admin'"><a href="Deal?action=deals">All_Deals</a></li>
			<li ng-if="'${sessionScope.role}' == 'admin'"><a href="PriceChange?action=start">Price</a></li>
			<li ng-if="'${sessionScope.role}' == 'admin'"><a href="User?action=start">Users</a></li>
			<li ng-if="'${sessionScope.role}' == ''"><a href="AuthorizationController">Login</a></li>
			<li	ng-if="'${sessionScope.role}' == 'admin' || '${sessionScope.role}' == 'Customer'"><a href="AuthorizationController?logout">Logout</a></li>
		</ul>
	</div>

	<h1>Fridges</h1>

	<br />

	<div class="message" ng-controller="alertMessagesController">
		<alert ng-repeat="alert in alerts" type="{{alert.type}}"
			close="closeAlert($index)">{{alert.msg}}</alert>
	</div>

	<br>

	<div class="grid2" ng-controller="fridgesListController">
		<div>
			<h3>Product List</h3>
		</div>

		<div class="gridStyle" ng-grid="gridOptions"></div>

		<pagination direction-links="true" boundary-links="true"
			total-items="fridges.totalResults" items-per-page="fridges.pageSize"
			ng-model="fridges.currentPage" ng-change="refreshGrid()">
		</pagination>
		<div>
			<form name="filterForm" ng-submit="sendFilter()" novalidate>

				<input type="hidden" name="thisIsFilter" ng-model="filter.thisIsFilter" ng-init="filter.thisIsFilter = true" />
				<input type="hidden" name="ftype" ng-model="filter.ftype" ng-init="filter.ftype = 'fridge'" />
				<div class="column">

					<div class="form-group"
						ng-class="{'has-error' : filterForm.fmodel.$invalid && filterForm.fmodel.$dirty}">
						<label for="fmodel">Model:</label> <span
							ng-class="{'glyphicon glyphicon-ok' : filterForm.fmodel.$valid && filterForm.fmodel.$dirty}"></span>

						<input id="fmodel" name="fmodel" type="text" class="form-control"
							maxlength="30" ng-model="filter.fmodel" />
					</div>

					<div class="form-group"
						ng-class="{'has-error' : filterForm.fworthMin.$invalid && filterForm.fworthMin.$dirty}">
						<label for="fworthMin">WorthMin:</label> <span
							ng-class="{'glyphicon glyphicon-ok' : filterForm.fworthMin.$valid && filterForm.fworthMin.$dirty}"></span>

						<input id="fworthMin" name="fworthMin" type="number"
							class="form-control" ng-model="filter.fworthMin"
							ng-pattern="/^[0-9]+$/" step="1">

					</div>

					<div class="form-group"
						ng-class="{'has-error' : filterForm.fworthMax.$invalid && filterForm.fworthMax.$dirty}">
						<label for="fworthMax">WorthMax:</label> <span
							ng-class="{'glyphicon glyphicon-ok' : filterForm.fworthMax.$valid && filterForm.fworthMax.$dirty}"></span>

						<input id="fworthMax" name="fworthMax" type="number"
							class="form-control" ng-model="filter.fworthMax"
							ng-pattern="/^[0-9]+$/" step="1">

					</div>

				</div>

				<div class="column">

					<div class="form-group"
						ng-class="{'has-error' : filterForm.fdealer.$invalid && filterForm.fdealer.$dirty}">
						<label for="fdealer">Dealer:</label> <span
							ng-class="{'glyphicon glyphicon-ok' : filterForm.fdealer.$valid && filterForm.fdealer.$dirty}"></span>

						<input id="fdealer" name="fdealer" type="text"
							class="form-control" maxlength="30" ng-model="filter.fdealer" />
					</div>

					<div class="form-group"
						ng-class="{'has-error' : filterForm.flengthMin.$invalid && filterForm.flengthMin.$dirty}">
						<label for="flengthMin">LengthMin:</label> <span
							ng-class="{'glyphicon glyphicon-ok' : filterForm.flengthMin.$valid && filterForm.flengthMin.$dirty}"></span>

						<input id="flengthMin" name="flengthMin" type="number"
							class="form-control" ng-model="filter.flengthMin"
							ng-pattern="/^[0-9]+$/" step="1">

					</div>

					<div class="form-group"
						ng-class="{'has-error' : filterForm.flengthMax.$invalid && filterForm.flengthMax.$dirty}">
						<label for="flengthMax">LengthMax:</label> <span
							ng-class="{'glyphicon glyphicon-ok' : filterForm.flengthMax.$valid && filterForm.flengthMax.$dirty}"></span>

						<input id="flengthMax" name="flengthMax" type="number"
							class="form-control" ng-model="filter.flengthMax"
							ng-pattern="/^[0-9]+$/" step="1">

					</div>

				</div>

				<div class="column">

					<div class="form-group"
						ng-class="{'has-error' : filterForm.fwidthMin.$invalid && filterForm.fwidthMin.$dirty}">
						<label for="fwidthMin">WidthMin:</label> <span
							ng-class="{'glyphicon glyphicon-ok' : filterForm.fwidthMin.$valid && filterForm.fwidthMin.$dirty}"></span>

						<input id="fwidthMin" name="fwidthMin" type="number"
							class="form-control" ng-model="filter.fwidthMin"
							ng-pattern="/^[0-9]+$/" step="1">

					</div>

					<div class="form-group"
						ng-class="{'has-error' : filterForm.fwidthMax.$invalid && filterForm.fwidthMax.$dirty}">
						<label for="fwidthMax">WidthMax:</label> <span
							ng-class="{'glyphicon glyphicon-ok' : filterForm.fwidthMax.$valid && filterForm.fwidthMax.$dirty}"></span>

						<input id="fwidthMax" name="fwidthMax" type="number"
							class="form-control" ng-model="filter.fwidthMax"
							ng-pattern="/^[0-9]+$/" step="1">

					</div>

				</div>

				<div class="column">
					<div class="form-group"
						ng-class="{'has-error' : filterForm.fspecialParameter2.$invalid && filterForm.fspecialParameter2.$dirty}">
						<label for="fspecialParameter2">Type:</label> <span
							ng-class="{'glyphicon glyphicon-ok' : filterForm.fspecialParameter2.$valid && filterForm.fspecialParameter2.$dirty}"></span>

						<select id="fspecialParameter2" name="fspecialParameter2"
							class="form-control" ng-model="filter.fspecialParameter2">
							<option value="">All</option>
							<option value="no_frost">No&nbsp;frost</option>
							<option value="drop_frost">Drop&nbsp;frost</option>
							<option value="manual">Manual</option>
						</select>

					</div>

					<div class="form-group"
						ng-class="{'has-error' : filterForm.fheightMin.$invalid && filterForm.fheightMin.$dirty}">
						<label for="fheightMin">HeightMin:</label> <span
							ng-class="{'glyphicon glyphicon-ok' : filterForm.fheightMin.$valid && filterForm.fheightMin.$dirty}"></span>

						<input id="fheightMin" name="fheightMin" type="number"
							class="form-control" ng-model="filter.fheightMin"
							ng-pattern="/^[0-9]+$/" step="1">

					</div>

					<div class="form-group"
						ng-class="{'has-error' : filterForm.fheightMax.$invalid && filterForm.fheightMax.$dirty}">
						<label for="fheightMax">HeightMax:</label> <span
							ng-class="{'glyphicon glyphicon-ok' : filterForm.fheightMax.$valid && filterForm.fheightMax.$dirty}"></span>

						<input id="fheightMax" name="fheightMax" type="number"
							class="form-control" ng-model="filter.fheightMax"
							ng-pattern="/^[0-9]+$/" step="1">

					</div>

				</div>

				<div style="display: inline-block">

					<div class="form-group" style="display: inline-block"
						ng-class="{'has-error' : filterForm.fspecialParameter1Min.$invalid && filterForm.fspecialParameter1Min.$dirty}">
						<label for="fspecialParameter1Min">VolumeMin:</label> <span
							ng-class="{'glyphicon glyphicon-ok' : filterForm.fspecialParameter1Min.$valid && filterForm.fspecialParameter1Min.$dirty}"></span>

						<input id="fspecialParameter1Min" name="fspecialParameter1Min"
							type="number" class="form-control"
							ng-model="filter.fspecialParameter1Min" ng-pattern="/^[0-9]+$/"
							step="1">

					</div>

					<div class="form-group" style="display: inline-block"
						ng-class="{'has-error' : filterForm.fspecialParameter1Max.$invalid && filterForm.fspecialParameter1Max.$dirty}">
						<label for="fspecialParameter1Max">VolumeMax:</label> <span
							ng-class="{'glyphicon glyphicon-ok' : filterForm.fspecialParameter1Max.$valid && filterForm.fspecialParameter1Max.$dirty}"></span>

						<input id="fspecialParameter1Max" name="fspecialParameter1Max"
							type="number" class="form-control"
							ng-model="filter.fspecialParameter1Max" ng-pattern="/^[0-9]+$/"
							step="1">

					</div>

				</div>

				<div class="buttons" style="display: inline-block">
					<button type="submit" class="btn btn-primary"
						ng-disabled="filterForm.$invalid">Filter</button>
				</div>
			</form>
		</div>
	</div>

	<div class="form2" ng-controller="fridgeFormController">
		<div ng-if="product.id == null">
			<h3>Choose&nbsp;fridge</h3>
		</div>
		<div ng-if="product.id != null">
			<h3>Enter&nbsp;count</h3>
		</div>

		<div>
			<form ng-if="product.id != null" name="productForm"
				ng-submit="addToCart()" novalidate>

				<div class="form-group"
					ng-class="{'has-error' : productForm.model.$invalid && productForm.model.$dirty}">
					<label for="model">Model:</label> <span
						ng-class="{'glyphicon glyphicon-ok' : productForm.model.$valid && productForm.model.$dirty}"></span>

					<label class="valueLabel">{{product.model}}</label>

				</div>

				<div class="form-group"
					ng-class="{'has-error' : productForm.dealer.$invalid && productForm.dealer.$dirty}">
					<label for="dealer">Dealer:</label> <span
						ng-class="{'glyphicon glyphicon-ok' : productForm.dealer.$valid && productForm.dealer.$dirty}"></span>

					<label class="valueLabel">{{product.dealer}}</label>

				</div>

				<div class="form-group"
					ng-class="{'has-error' : productForm.worth.$invalid && productForm.worth.$dirty}">
					<label for="worth">Worth:</label> <span
						ng-class="{'glyphicon glyphicon-ok' : productForm.worth.$valid && productForm.worth.$dirty}"></span>

					<label class="valueLabel">{{product.worth}}</label>

				</div>

				<div class="form-group"
					ng-class="{'has-error' : productForm.length.$invalid && productForm.length.$dirty}">
					<label for="length">Length:</label> <span
						ng-class="{'glyphicon glyphicon-ok' : productForm.length.$valid && productForm.length.$dirty}"></span>

					<label class="valueLabel">{{product.length}}</label>

				</div>

				<div class="form-group"
					ng-class="{'has-error' : productForm.width.$invalid && productForm.width.$dirty}">
					<label for="width">Width:</label> <span
						ng-class="{'glyphicon glyphicon-ok' : productForm.width.$valid && productForm.width.$dirty}"></span>

					<label class="valueLabel">{{product.width}}</label>

				</div>

				<div class="form-group"
					ng-class="{'has-error' : productForm.height.$invalid && productForm.height.$dirty}">
					<label for="height">Height:</label> <span
						ng-class="{'glyphicon glyphicon-ok' : productForm.height.$valid && productForm.height.$dirty}"></span>

					<label class="valueLabel">{{product.height}}</label>

				</div>

				<div class="form-group"
					ng-class="{'has-error' : productForm.specialParameter1.$invalid && productForm.specialParameter1.$dirty}">
					<label for="specialParameter1">Volume:</label> <span
						ng-class="{'glyphicon glyphicon-ok' : productForm.specialParameter1.$valid && productForm.specialParameter1.$dirty}"></span>

					<label class="valueLabel">{{product.specialParameter1}}</label>

				</div>

				<div class="form-group"
					ng-class="{'has-error' : productForm.specialParameter2.$invalid && productForm.specialParameter2.$dirty}">
					<label for="specialParameter2">Type:</label> <span
						ng-class="{'glyphicon glyphicon-ok' : productForm.specialParameter2.$valid && productForm.specialParameter2.$dirty}"></span>

					<label class="valueLabel">{{product.specialParameter2}}</label>

				</div>

				<div class="form-group"
					ng-class="{'has-error' : productForm.count.$invalid && productForm.count.$dirty}">
					<label for="count">Count:</label> <span
						ng-class="{'glyphicon glyphicon-ok' : productForm.count.$valid && productForm.count.$dirty}"></span>

					<input id="count" name="count" type="number" class="form-control"
						min="1" max="1000" ng-model="product.count"
						ng-pattern="/^[0-9]+$/" step="1" required>

					<p class="help-block" ng-show="productForm.length.$error.required">Add&nbsp;length.</p>
					<p class="help-block" ng-show="productForm.length.$error.min">Minimum&nbsp;value&nbsp;=1.</p>
					<p class="help-block" ng-show="productForm.length.$error.max">Max&nbsp;value&nbsp;=1000.</p>
				</div>

				<div class="buttons">
					<button type="button" class="btn btn-primary"
						ng-click="clearForm()">Clear</button>
					<button type="submit" class="btn btn-primary"
						ng-disabled="productForm.$invalid">Save</button>
				</div>
				<br/>

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