<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Admin Dashboard</title>
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
                <c:if test="${pageContext.request.userPrincipal.name != null}">
                    <h4>Hello : ${pageContext.request.userPrincipal.name}
                        |<a href="<c:url value="/j_spring_security_logout" />" > Logout</a>
                    </h4>
                </c:if>
            <div class="row">
                <div class="col-md-6">
                    <h2>Users</h2>
                    <table id="userTable" class="table table-hover">
                        <tr>
                            <th width="60%">Username</th>
                            <th width="40%"><a href="${pageContext.request.contextPath}/displayUserForm">Create User</a></th>
                        </tr>
                        <c:forEach var="currentUser" items="${userList}">
                            <tr>
                                <td>
                                    <c:out value="${currentUser.username}"/>
                                </td>
                                <td><a href="deleteUser?id=${currentUser.id}">Delete User</a></td>
                            </tr>
                        </c:forEach>
                    </table>
                </div>
                <div class="col-md-6">
                    <h2>Posts Pending Approval</h2>
                    <table id="postTable" class="table table-hover">
                        <tr>
                            <th width="60%">Title</th>
                            <th width="40%"></th>
                        </tr>
                        <c:forEach var="currentArticle" items="${pendingList}">
                            <tr>
                                <td>
                                    <c:out value="${currentArticle.title}"/>
                                </td>
                                <td>
                                    <a href="displayApprovalForm?articleId=${currentArticle.id}">Preview</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <h2>Static Pages</h2>
                    <table id="staticTable" class="table table-hover">
                        <tr>
                            <th width="60%">Page Name</th>
                            <th width="40%"><a href="${pageContext.request.contextPath}/displayAddPage">Create Page</a></th>
                        </tr>
                        <c:forEach var="currentPage" items="${pageList}">
                            <tr>
                                <td>
                                    <c:out value="${currentPage.title}"/>
                                </td>
                                <td><a href="deletePage?id=${currentPage.id}">Delete Page</a></td>
                            </tr>
                        </c:forEach>
                    </table>
                </div>
            </div>
        </div>
    </body>
</html>
