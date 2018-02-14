<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html ng-app="regs">
<head>
<title>Registration</title>

<link rel="stylesheet" href="lib/dependencies/css/bootstrap.min.css" />
<link rel="stylesheet" href="lib/dependencies/css/ng-grid.min.css" />

<link rel="stylesheet" type="text/css" href="css/style.css" />

<script src="lib/dependencies/angular.min.js"></script>
<script src="lib/dependencies/angular-resource.min.js"></script>
<script src="lib/dependencies/angular-route.min.js"></script>
<script src="lib/dependencies/jquery.min.js"></script>
<script src="lib/dependencies/ui-bootstrap-tpls.min.js"></script>
<script src="lib/dependencies/ng-grid.min.js"></script>

<script src="js/register.js"></script>

</head>

<body>

	<div style="width: 100px; display: inline-block; float: left">
		<div id="wrapper">
			<ul class="menu2">
				<li><a href="RegistrationController?locale=eng"><img
						src="https://purepetfood.co.uk/catalog/view/theme/pure/image/uk-small.png"
						style="height: 20px; width: 40px" /></a></li>
				<li><a href="RegistrationController?locale=ru"><img
						src="https://upload.wikimedia.org/wikipedia/en/thumb/f/f3/Flag_of_Russia.svg/1200px-Flag_of_Russia.svg.png"
						style="height: 20px; width: 40px" /></a></li>
			</ul>
		</div>
	</div>

	<div id="wrapper">
		<ul class="menu">
			<li><a href="AuthorizationController">Логин</a></li>
		</ul>
	</div>

	<div class="message">
		<c:if test="${param.error ne null}">
			<alert class="alert-danger">Этот емейл уже используется.</alert>
		</c:if>
		<c:if test="${param.success ne null}">
			<alert class="alert-success">Вы успешно зарегестрировались.</alert>
		</c:if>
	</div>

	<div class="form">
		<div class="contextname">
			<h3>Регистрация.</h3>
		</div>

		<div>
			<form action="RegistrationController" method="post">
				<div class="form-group"
					ng-class="{'has-error':regForm.mail.$invalid && regForm.mail.$dirty}">
					<label for="mail">Емейл:</label> <span
						ng-class="{'glyphicon glyphicon-ok' :regForm.mail.$valid && regForm.mail.$dirty}"></span>

					<input id="mail" name="name" type="email" class="form-control"
						maxlength="50" ng-model="register.mail" required ng-maxlength="50" />

					<!-- <p class="help-block" ng-show="regForm.mail.$error.required">Enter email.</p> -->
					<p class="help-block" ng-show="regForm.mail.$error.maxlength">Емейл
						не может быть длинее 50и символов.</p>
				</div>

				<div class="form-group"
					ng-class="{'has-error':regForm.password.$invalid && regForm.password.$dirty}">
					<label for="password">Пароль:</label> <span
						ng-class="{'glyphicon glyphicon-ok' :regForm.password.$valid && regForm.password.$dirty}"></span>

					<input id="password" name="password" type="password"
						class="form-control" ng-model="register.password" required
						ng-minlength="6" ng-maxlength="20" />

					<p class="help-block" ng-show="regForm.password.$error.minlength">Минимальная длина пароля 6 символов.</p>
					<p class="help-block" ng-show="regForm.password.$error.maxlength">Максимальная длина пароля 6 символов.</p>
				</div>

				<div class="form-group"
					ng-class="{'has-error':regForm.cpassword.$invalid && regForm.cpassword.$dirty}">
					<label for="password">Повторите пароль:</label> <span
						ng-class="{'glyphicon glyphicon-ok' :regForm.cpassword.$valid && regForm.cpassword.$dirty && (register.password == cpassword)}"></span>

					<input id="cpassword" name="cpassword" type="password"
						class="form-control" ng-model="cpassword" required />
					<p class="help-block" ng-show="register.password != cpassword">Пароли должны совпадать.</p>
				</div>


				<div class="buttons">
					<button type="submit" class="btn btn-primary"
						ng-disabled="regForm.$invalid || register.password != cpassword">Подтвердить</button>
				</div>

			</form>

		</div>
	</div>

</body>
</html>