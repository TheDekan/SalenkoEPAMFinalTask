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
			<li><a href="RegistrationController">Register</a></li>
			<li	ng-if="'${sessionScope.role}' == 'admin' || '${sessionScope.role}' == 'Customer'"><a href="AuthorizationController?logout">Logout</a></li>
		</ul>
  </div>

  <div class="message">
    <c:if test="${param.error ne null}">
      <alert class="alert-danger">Invalid email or password.</alert>
    </c:if>
    <c:if test="${param.blocked ne null}">
      <alert class="alert-danger">Your account is blocked.</alert>
    </c:if>
    <c:if test="${param.logout ne null}">
      <alert class="alert-success">You have been logged out.</alert>
    </c:if>
  </div>

  <div class="form">
    <div class="contextname">
      <h3>Authorisation form.</h3>
    </div>

    <div>
      <form action="AuthorizationController" method="post">

        <div class="form-group"
          ng-class="{'has-error':authForm.mail.$invalid && authForm.mail.$dirty}">
          <label for="mail">Email:</label> <span
            ng-class="{'glyphicon glyphicon-ok' :authForm.mail.$valid && authForm.mail.$dirty}"></span>

          <input type="text" name="name" placeholder="Email"
            class="form-control" maxlength="50" ng-model="name" required
            ng-maxlength="50" />


        </div>

        <div class="form-group"
          ng-class="{'has-error':authForm.password.$invalid && authForm.password.$dirty}">
          <label for="password">Password:</label> <span
            ng-class="{'glyphicon glyphicon-ok' :authForm.password.$valid && authForm.password.$dirty}"></span>

          <input type="password" name="password" placeholder="Password"
            class="form-control" ng-model="password" required
            ng-minlength="6" ng-maxlength="20" />
        </div>


        <div class="buttons">
          <button type="submit" class="btn btn-primary" value="Sign In"
                    ng-disabled="authForm.$invalid">Confirm</button>
        </div>

      </form>

    </div>
  </div>
  
  


</body>
</html>