<html>
<head>
    <script type="text/javascript">
        <jsp:include page="_castle_script.jsp"/>
    </script>
</head>
<body>
<h2>Welcome to Castle World</h2>
<form action="login" method="POST">
    <div>
        <div class="input-prepend input-block-level">
            <span class="add-on"><i class="icon-user"></i></span>
            <input type="text" placeholder="Login" autocorrect="off" autocapitalize="off" autocomplete="off" spellcheck="false" id="j_username" name="username">
        </div>
    </div>
    <div>
        <div class="input-prepend input-block-level">
            <span class="add-on"><i class="icon-lock"></i></span>
            <input type="password" placeholder="Password" autocorrect="off" autocapitalize="off" autocomplete="off" spellcheck="false" id="j_password" name="password">
        </div>
    </div>
    <div>
        <input type="submit" class="btn" value="Login" name="submit">
    </div>
</form>
</body>
</html>
