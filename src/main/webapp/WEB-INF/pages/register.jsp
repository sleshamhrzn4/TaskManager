<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/register.css">
</head>
<body>
<div class="container">
    <div class=" box">
        <div class=" top-bar">
            <span>Already have an Account? </span>
            <a href="${pageContext.request.contextPath}/login">
                <button class=" login-btn">Login</button>

            </a>

        </div>


        <div class = "form-container">
            <form action=" ${pageContext.request.contextPath}/register" method="post" >
                <label>
                    <input type="text" name="userName" placeholder="Full Name" value="${oldUserName}"/>
                </label>

                <c:if test="${not empty errors.userName}">
                    <span class="error">${errors.userName}</span>

                </c:if>

                <label>
                    <input type = "text"  name="userEmail" placeholder="Email" value="${oldUserEmail}"/>

                </label>

                <c:if test="${not empty errors.userEmail}">
                    <span class="error"> ${errors.userEmail}</span>
                </c:if>

                <input type="password" name="password" placeholder="Password">
                <c:if test="${not empty errors.password}">
                    <span class="error">${errors.password}</span>
                </c:if>

                <button type="submit" class="signup-btn">Sign Up</button>



            </form>
            <p class="terms">
                By signing up, you agree to our Terms and Privacy Policy
            </p>

        </div>
    </div>

</div>

</body>
</html>
