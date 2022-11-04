<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Home</title>
        <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">        
        <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/css/capstoneblog.css" rel="stylesheet">
        <style>
            .module {
                overflow: hidden;
                width: 100%; 
                display: inline-block;
                text-overflow: ellipsis;
            }
        </style>
    </head>
    <body>
        <jsp:include page="/jsp/navbar.jsp" />
        <div class="container">
            <br/>
            <br/>
            <br/>
            <h1>Most Recent Articles</h1>
            <br/>
            <div class="container-fluid">
                <c:if test="${pageContext.request.userPrincipal.name != null}">
                    <h4>Hello : ${pageContext.request.userPrincipal.name}
                        |<a href="<c:url value="/j_spring_security_logout" />" > Logout</a>
                    </h4>
                </c:if>
                <c:forEach var="currentArticle" items="${articleList}">
                    <c:if test="${currentArticle.approvalId == 2}">
                        <div class="row align-items-center">
                            <div class="col-md-6">
                                <img src="<c:out value="${currentArticle.imageLink}"/>" class="img-fluid img-thumbnail">
                            </div>
                            <div class="col-md-6">
                                <h2 id="articleTitle">
                                    <a href="displayArticleDetails?articleId=${currentArticle.id}"/>
                                        <c:out value="${currentArticle.title}"/>
                                    </a>
                                </h2>
                                <p>
                                    Written By: <c:out value="${currentArticle.author.username}"/> on <c:out value="${currentArticle.date}"/>
                                    <sec:authorize access="hasRole('ROLE_ADMIN')">
                                        (<a href="${pageContext.request.contextPath}/deletePost?id=${currentArticle.id}">Delete</a>)
                                    </sec:authorize>
                                </p>
                                <div class="module">
                                    <p><c:out value="${currentArticle.content}" escapeXml="false"/></p>
                                </div>
                                <p>Tags:
                                    <c:choose>
                                        <c:when test="${currentArticle.categories == null}">

                                        </c:when>
                                        <c:otherwise>
                                            <c:forEach var="currentCat" items="${currentArticle.categories}">
                                                <a href="displayMatchingArticles?categoryId=${currentCat.categoryId}"><c:out value="${currentCat.name}"/></a>
                                            </c:forEach>
                                        </c:otherwise>
                                    </c:choose>
                                </p>
                            </div>
                        </div>
                    </c:if>
                </c:forEach>
            </div>
        </div>
        <!-- Placed at the end of the document so the pages load faster -->
        <script src="${pageContext.request.contextPath}/js/jquery-3.1.1.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>

    </body>
</html>

