<#include "security.ftlh">
<#import "login.ftlh" as l>

<nav class="navbar navbar-expand-lg bg-body-tertiary">
    <div class="container-fluid">
        <a class="navbar-brand" href="/">PASTEBIN <small>by psyleks</small></a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
                aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link" href="/">Home</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/main">Messages</a>
                </li>
                <#if isAdmin>
                    <li class="nav-item">
                        <a class="nav-link" href="/user">User list</a>
                    </li>
                </#if>
            </ul>

            <#if known>
                <li class="nav-item dropstart me-3">
                    <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown"
                       aria-expanded="false">
                        ${name}
                    </a>
                    <ul class="dropdown-menu">
                        <li>
                            <a class="dropdown-item" href="/user/profile">Profile</a>
                        </li>
                        <li>
                            <form action="/logout" method="post">
                                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                                <button class="dropdown-item" style="color:Red" type="submit">Log Out</button>
                            </form>
                        </li>
                    </ul>
                </li>
            <#else>
                <a href="/login" class="btn btn-primary" role="button">Sign In</a>
            </#if>


        </div>
    </div>
</nav>

