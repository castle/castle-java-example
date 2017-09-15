<html>
<head>
    <script type="text/javascript">
        <jsp:include page="_castle_script.jsp"/>
    </script>
    <title>Reset Your Password</title>
</head>
<body>
<h2>We will send you a link to the email address linked to your account.</h2>
<form action="/password_reset_required" method="POST">
    <div>
        <div class="input-prepend input-block-level">
            <span class="add-on"><i class="icon-user"></i></span>
            <input type="text" placeholder="Login" autocorrect="off" autocapitalize="off" autocomplete="off"
                   spellcheck="false" id="j_login" name="login">
            <input type="submit" value="Send"/>
        </div>
    </div>
</form>
<div>
    <form method="post">
        <button formaction="/">Cancel</button>
    </form>
</div>
</body>
</html>