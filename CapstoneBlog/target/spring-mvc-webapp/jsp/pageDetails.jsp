<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Article</title>
        <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">        
        <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/css/capstoneblog.css" rel="stylesheet">
    </head>
    <body>
        <jsp:include page="/jsp/navbar.jsp" />
        <br/>
        <br/>
        <br/>
        <br/>
        <div class="container">
            <c:if test="${pageContext.request.userPrincipal.name != null}">
                <h4 style="text-align: right">Hello ${pageContext.request.userPrincipal.name}
                    | <a href="<c:url value="/j_spring_security_logout" />" > Logout</a>
                </h4>
            </c:if>
                
            <div class="container-fluid">
                <div class="row align-items-center">
                    <div class="col text-center">
                        <h2 id="pageTitle">
                            <c:out value="${page.title}"/>
                        </h2>
                        <p>
                            Written By: <c:out value="${page.author.username}"/> on <c:out value="${page.date}"/>
                                <sec:authorize access="hasRole('ROLE_ADMIN')">
                                (<a href="${pageContext.request.contextPath}/deletePage?id=${page.id}">Delete</a>)
                                </sec:authorize>
                        </p>
                        <div class="module">
                            <p><c:out value="${page.content}" escapeXml="false"/></p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>

