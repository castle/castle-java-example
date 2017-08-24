<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script type="text/javascript">
        <jsp:include page="_castle_script.jsp"/>
    </script>
    <title>Reset Password Challenge</title>
</head>
<body>
<h2>We have sent you an email with details for resetting your password. Did you receive it?</h2>

<form id="j_password_reset">
    <button formaction="/password_reset_form.jsp" formmethod="get" type="submit" value=>Yes, and I confirm I have control over that email account.</button><br>
    <button name="password_reset" formaction="/password_reset" formmethod="post" type="submit" value=false>Something is wrong. I did not request a password reset.</button>
</form>

</body>
</html>
