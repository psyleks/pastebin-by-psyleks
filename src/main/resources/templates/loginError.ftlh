<#import "login.ftlh" as e>

<@e.c.page>
    <div class="alert alert-danger" role="alert">
        Bad credentials
    </div>
    <@e.l.login "/login" false/>
</@e.c.page>

