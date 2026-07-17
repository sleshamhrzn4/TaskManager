<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">

    <title>Document</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
</head>
<body>
<div class="constainer">

    <div class="right">
        <div class=" top-bar">
            <span>Don't have an Account? </span>
            <a href="${pageContext.request.contextPath}/register">
                <button class=" login-btn">Register</button>

            </a>

        </div>
        <div class="form-container">
            <h4>Connect with us</h4>

            <c:if test="${not empty errorMessage}">
                <div class="error-msg">${errorMessage}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/login"
                  method="post">
            <input type="email" name="email" placeholder="Email Address"
                   required> <input type="password" name="password"
                                    placeholder="Password" required>
            <button type="submit" class="login-btn">Log In</button>
            </form>

        </div>

    </div>

</div>

</body>
</html>