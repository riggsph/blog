<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!-- Custom CSS -->
<link href="${pageContext.request.contextPath}/css/navigation.css" rel="stylesheet">

<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/">Blue's Blog</a>
        <div class="navbar-header">

            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
        </div>
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">

            <ul class="nav navbar-nav">

                <li class="nav-divider"></li>

                    <li role="presentation"><a href="${pageContext.request.contextPath}/">Home</a></li>
                <c:forEach var="currentPage" items="${pageList}">
                    <li><a href="displayPage?pageId=${currentPage.id}"/><c:out value="${currentPage.title}"/></a></li>
                </c:forEach>
                <sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_EDITOR')">
                    <li role="presentation"><a href="${pageContext.request.contextPath}/displayPostingPage">New Post</a></li>
                </sec:authorize>
                <sec:authorize access="hasRole('ROLE_ADMIN')">
                    <li role="presentation"><a href="${pageContext.request.contextPath}/displayAdminPage">Admin</a></li>
                </sec:authorize>
                <c:if test="${pageContext.request.userPrincipal.name == null}">
                    <li role="presentation"><a href="${pageContext.request.contextPath}/login">Login</a></li>
                </c:if>
            </ul>
                
        </div>
        <!-- /.navbar-collapse -->
    </div>
    <!-- /.container -->
</nav>
