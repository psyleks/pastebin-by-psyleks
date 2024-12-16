<#include "parts/security.ftlh">
<#import "parts/common.ftlh" as c>

<@c.page>
<h5>Hello, </h5>
    <#if known>
        <h3>${name}</h3>
        <#else>
        <h5>user</h5>
    </#if>
</@c.page>