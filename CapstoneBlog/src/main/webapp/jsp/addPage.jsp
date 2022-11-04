<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>New Page</title>
        <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/css/capstoneblog.css" rel="stylesheet">
    </head>
    <body>
        <jsp:include page="/jsp/navbar.jsp" />
        <br/>
        <br/>
        <div class="container">
            <h1 class="text-center">Create a New Page</h1>
            <div>
                <c:if test="${pageContext.request.userPrincipal.name != null}">
                    <h4>Hello : ${pageContext.request.userPrincipal.name}
                        |<a href="<c:url value="/j_spring_security_logout" />" > Logout</a>
                    </h4>
                </c:if>
                <form class="form-horizontal" role="form" method="post" action="createPage">
                    <div class="form-group">
                        <label for="new-post-title" class="col-md-4 control-label title-label">Title:</label>
                        <div class="col-md-6">
                            <input type="text" class="form-control" name="title" placeholder="Title" required/>
                        </div> 
                    </div>
                    <div class="form-group">
                        <textarea name="content" id="newBlogPost">This is a test of TinyMCE editor!</textarea>
                    </div>
                    <div class="form-group">
                        <label for="post-date" class="col-md-4 control-label">Date:</label> 
                        <div class="col-md-6">
                            <input type="date" class="form-control" name="date" required/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="post-expiration-date" class="col-md-4 control-label">Expiration Date:</label> 
                        <div class="col-md-6">
                            <input type="date" class="form-control" name="expirationDate"/>Not required!
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-offset-4 col-md-4">
                            <input type="submit" class="btn btn-default" value="Post!"/>
                        </div>
                    </div>
                </form>
            </div>    
        </div>

        <script src="https://cdn.tiny.cloud/1/dxomf1u6xpdm88qvnuy9le6u396z7q1a68msl4fpjhmdudat/tinymce/5/tinymce.min.js" referrerpolicy="origin"></script>
        <script>
            tinymce.init({
                selector: '#newBlogPost',
                height: 500,
                toolbar: 'undo redo | styleselect | bold italic | alignleft aligncenter alightright alignjustify\n\
                | bullist numlist outdent indent',
                toolbar_mode: 'floating',
                tinycomments_mode: 'embedded',
                tinycomments_author: 'Author name'
            });
        </script>
    </body>
</html>

