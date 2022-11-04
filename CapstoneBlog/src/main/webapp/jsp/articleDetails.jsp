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
                <h4>Hello : ${pageContext.request.userPrincipal.name}
                    |<a href="<c:url value="/j_spring_security_logout" />" > Logout</a>
                </h4>
            </c:if>
                
            <div class="container-fluid">
                <c:if test="${article.approvalId == 2}">
                    <div class="row align-items-center">
                        <div class="col-md-6">
                            <img src="<c:out value="${article.imageLink}"/>" class="img-fluid img-thumbnail">
                        </div>
                        <div class="col-md-6">
                            <h2 id="articleTitle">
                                <a href="displayArticleDetails?articleId=${article.id}"/>
                                    <c:out value="${article.title}"/>
                                </a>
                            </h2>
                            <p>
                                Written By: <c:out value="${article.author.username}"/> on <c:out value="${article.date}"/>
                                    <sec:authorize access="hasRole('ROLE_ADMIN')">
                                        (<a href="${pageContext.request.contextPath}/deletePost?id=${currentArticle.id}">Delete</a>)
                                    </sec:authorize>
                            </p>
                            <div class="module">
                                <p><c:out value="${article.content}" escapeXml="false"/></p>
                            </div>
                            <p>Tags:
                                <c:choose>
                                    <c:when test="${article.categories == null}">

                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="currentCat" items="${article.categories}">
                                            <a href="displayMatchingArticles?categoryId=${currentCat.categoryId}"><c:out value="${currentCat.name}"/></a>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </p>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
    </body>
</html>
