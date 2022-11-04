<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Category Search</title>
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
            <h1>Articles with Matching Tag</h1>
            <c:if test="${pageContext.request.userPrincipal.name != null}">
                <h4 style="text-align: right">Hello ${pageContext.request.userPrincipal.name}
                    | <a href="<c:url value="/j_spring_security_logout" />" > Logout</a>
                </h4>
            </c:if>
            <div class="row">
                <table id="userTable" class="table table-dark table-hover">
                    <tr>
                        <th width="50%">Title</th>
                        <th width="50%"></th>
                    </tr>
                    <c:forEach var="currentArticle" items="${matchingList}">
                        <tr>
                            <td>
                                <a href="displayArticleDetails?articleId=${currentArticle.id}"/><c:out value="${currentArticle.title}"/></a>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
        </div>
    </body>
</html>
