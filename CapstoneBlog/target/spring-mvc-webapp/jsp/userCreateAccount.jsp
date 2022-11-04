<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Create Account</title>
        <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/css/capstoneblog.css" rel="stylesheet">
    </head>
    <body>
        <jsp:include page="/jsp/navbar.jsp" />
        <br/>
        <br/>
        <br/>
        <div class="container">
            <h1>Create New Account</h1>
                <c:if test="${pageContext.request.userPrincipal.name != null}">
                    <h4>Hello : ${pageContext.request.userPrincipal.name}
                        |<a href="<c:url value="/j_spring_security_logout" />" > Logout</a>
                    </h4>
                </c:if>
            <br/>
            <form class="form-horizontal" 
                    role="form" 
                    method="post" 
                    action="createNewUser">
                <div class="form-group">
                    <label for="username" class="col-md-4 control-label">Username:</label>
                    <div class="col-md-8">
                        <input type="text" class="form-control" name="username" placeholder="Username"/>
                  </div>
                </div>
                <div class="form-group">
                    <label for="password" class="col-md-4 control-label">Password:</label>
                    <div class="col-md-8">
                        <input type="password" class="form-control" name="password" placeholder="Password"/>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-md-offset-4 col-md-8">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <input type="hidden" name="role" value="user"/>
                        <input type="submit" class="btn btn-default" id="search-button" value="Submit"/>
                    </div>
                </div>
        </div>
        <script src="${pageContext.request.contextPath}/js/jquery-3.1.1.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
    </body>
</html>
