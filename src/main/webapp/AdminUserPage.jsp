<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html ng-app="user">
<head>
<title>User control page</title>

<link rel="stylesheet" href="lib/dependencies/css/bootstrap.min.css" />
<link rel="stylesheet" href="lib/dependencies/css/ng-grid.min.css" />

<link rel="stylesheet" type="text/css" href="css/style4.css" />

<script src="lib/dependencies/angular.min.js"></script>
<script src="lib/dependencies/angular-resource.min.js"></script>
<script src="lib/dependencies/angular-route.min.js"></script>
<script src="lib/dependencies/jquery.min.js"></script>
<script src="lib/dependencies/ui-bootstrap-tpls.min.js"></script>
<script src="lib/dependencies/ng-grid.min.js"></script>

<script src="js/user.js"></script>
</head>

<body>

	<div style="width: 100px; display:inline-block; float:left">
		<div id="wrapper">
			<ul class="menu2">
				<li><a href="User?action=start&locale=eng"><img src="https://purepetfood.co.uk/catalog/view/theme/pure/image/uk-small.png" style="height: 20px; width: 40px"/></a></li>
				<li><a href="User?action=start&locale=ru"><img src="https://upload.wikimedia.org/wikipedia/en/thumb/f/f3/Flag_of_Russia.svg/1200px-Flag_of_Russia.svg.png" style="height: 20px; width: 40px"/></a></li>
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

	<h1>User control page</h1>

	<br />

	<div class="message" ng-controller="alertMessagesController">
		<alert ng-repeat="alert in alerts" type="{{alert.type}}"
			close="closeAlert($index)">{{alert.msg}}</alert>
	</div>

	<br>

	<div class="grid2" ng-controller="usersListController">
		<div>
			<h3>User List</h3>
		</div>

		<div class="gridStyle" ng-grid="gridOptions"></div>

		<pagination direction-links="true" boundary-links="true"
			total-items="user.totalResults" items-per-page="user.pageSize"
			ng-model="user.currentPage" ng-change="refreshGrid()"> </pagination>

		<div>
			<form name="filterForm" ng-submit="sendFilter()" novalidate>

				<input type="hidden" name="thisIsFilter" ng-model="filter.thisIsFilter" ng-init="filter.thisIsFilter = true" />

				<div class="form-group"
					ng-class="{'has-error' : filterForm.fname.$invalid && filterForm.fname.$dirty}">
					<label for="fname">Email:</label> <span
						ng-class="{'glyphicon glyphicon-ok' : filterForm.fname.$valid && filterForm.fname.$dirty}"></span>

					<input id="fname" name="fname" type="text" class="form-control"
						maxlength="30" ng-model="filter.fname" />
				</div>

				<div class="buttons" style="display: inline-block">
					<button type="submit" class="btn btn-primary"
						ng-disabled="filterForm.$invalid">Filter</button>
				</div>
			</form>
		</div>

	</div>

	<div class="form2" ng-controller="userFormController">
		<div ng-if="user.id == null">
			<h3>Choose user</h3>
		</div>
		<div ng-if="user.id != null">
			<h3>User items</h3>
		</div>

		<div>
			<form ng-if="user.id != null" name="userForm"
				ng-submit="updateUserItem()" novalidate>

				<div class="form-group"
					ng-class="{'has-error' : userForm.name.$invalid && userForm.name.$dirty}">
					<label for="name">Email:</label> <span
						ng-class="{'glyphicon glyphicon-ok' : userForm.name.$valid && userForm.name.$dirty}"></span>

					<label class="valueLabel">{{user.name}}</label>

				</div>

				<div class="form-group"
					ng-class="{'has-error' : userForm.role.$invalid && userForm.role.$dirty}">
					<label for="role">Role:</label> <span
						ng-class="{'glyphicon glyphicon-ok' : userForm.role.$valid && userForm.role.$dirty}"></span>

					<label class="valueLabel">{{user.role}}</label>

				</div>

				<div class="form-group"
					ng-class="{'has-error' : userForm.joinDate.$invalid && userForm.joinDate.$dirty}">
					<label for="joinDate">Join&nbsp;date:</label> <span
						ng-class="{'glyphicon glyphicon-ok' : userForm.joinDate.$valid && userForm.joinDate.$dirty}"></span>

					<label class="valueLabel">{{user.joinDate}}</label>

				</div>

				<div class="form-group"
					ng-class="{'has-error' : userForm.blocked.$invalid && userForm.blocked.$dirty}">
					<label for="blocked">Block:</label> <span
						ng-class="{'glyphicon glyphicon-ok' : userForm.blocked.$valid && userForm.blocked.$dirty}"></span>

					<select id="blocked" name="blocked" class="form-control"
						ng-model="user.blocked">
						<option ng-selected="user.blocked === 'true'" value="true">true</option>
						<option ng-selected="user.blocked === 'false'" value="false">false</option>
					</select>

				</div>


				<div class="buttons">
					<button type="button" class="btn btn-primary"
						ng-click="clearForm()">Clear</button>
					<button type="submit" class="btn btn-primary"
						ng-disabled="userForm.$invalid">Save</button>
				</div>

			</form>
		</div>
	</div>

</body>
</html>