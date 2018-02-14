<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html ng-app="auth">
<head>
<title>Auth page</title>

<link rel="stylesheet" href="lib/dependencies/css/bootstrap.min.css" />
<link rel="stylesheet" href="lib/dependencies/css/ng-grid.min.css" />

<link rel="stylesheet" type="text/css" href="css/style.css" />

<script src="lib/dependencies/angular.min.js"></script>
<script src="lib/dependencies/angular-resource.min.js"></script>
<script src="lib/dependencies/jquery.min.js"></script>
<script src="lib/dependencies/ui-bootstrap-tpls.min.js"></script>
<script src="lib/dependencies/ng-grid.min.js"></script>

<script src="js/auth.js"></script>
</head>

<body>

	<div style="width: 100px; display:inline-block; float:left">
		<div id="wrapper">
			<ul class="menu2">
				<li><a href="AuthorizationController?locale=eng"><img src="https://purepetfood.co.uk/catalog/view/theme/pure/image/uk-small.png" style="height: 20px; width: 40px"/></a></li>
				<li><a href="AuthorizationController?locale=ru"><img src="https://upload.wikimedia.org/wikipedia/en/thumb/f/f3/Flag_of_Russia.svg/1200px-Flag_of_Russia.svg.png" style="height: 20px; width: 40px"/></a></li>
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
			<li><a href="RegistrationController">Регистрация</a></li>
			<li	ng-if="'${sessionScope.role}' == 'admin' || '${sessionScope.role}' == 'Customer'"><a href="AuthorizationController?logout">Логаут</a></li>
		</ul>
  </div>

  <div class="message">
    <c:if test="${param.error ne null}">
      <alert class="alert-danger">Неправильный емейл или пароль.</alert>
    </c:if>
    <c:if test="${param.blocked ne null}">
      <alert class="alert-danger">Ваш аккаунт заблокирован.</alert>
    </c:if>
    <c:if test="${param.logout ne null}">
      <alert class="alert-success">Вы покинули учетную запись.</alert>
    </c:if>
  </div>

  <div class="form">
    <div class="contextname">
      <h3>Форма авторизации.</h3>
    </div>

    <div>
      <form action="AuthorizationController" method="post">

        <div class="form-group"
          ng-class="{'has-error':authForm.mail.$invalid && authForm.mail.$dirty}">
          <label for="mail">Емейл:</label> <span
            ng-class="{'glyphicon glyphicon-ok' :authForm.mail.$valid && authForm.mail.$dirty}"></span>

          <input type="text" name="name" placeholder="Email"
            class="form-control" maxlength="50" ng-model="name" required
            ng-maxlength="50" />


        </div>

        <div class="form-group"
          ng-class="{'has-error':authForm.password.$invalid && authForm.password.$dirty}">
          <label for="password">Пароль:</label> <span
            ng-class="{'glyphicon glyphicon-ok' :authForm.password.$valid && authForm.password.$dirty}"></span>

          <input type="password" name="password" placeholder="Password"
            class="form-control" ng-model="password" required
            ng-minlength="6" ng-maxlength="20" />
        </div>


        <div class="buttons">
          <button type="submit" class="btn btn-primary" value="Sign In"
                    ng-disabled="authForm.$invalid">Подтвердить</button>
        </div>

      </form>

    </div>
  </div>
  
  


</body>
</html>