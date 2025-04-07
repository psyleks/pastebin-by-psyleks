<#import "parts/common.ftlh" as c>

<@c.page>
    <a href="/main">
        <button type="button" class="btn btn-outline-primary">Back</button>
    </a>
    <div class="card my-3">
        <div class="row g-0">
            <#if message.filename??>
            <div class="col-md-4">
                <img src="/uploads/${message.filename}" class="img-fluid rounded-start" alt="...">
            </div>
            </#if>
            <div class="col-md-8">
                <div class="card-body">
                    <h4 class="card-text">${message.authorName}</h4>
                    <small class="text-muted">${formattedDate}</small>
                    <hr style="margin-top: 10px; margin-bottom: 10px;">
                    <h5 class="card-title">${message.tag}</h5>
                    <p class="card-text">${message.text}</p>
                    <form action="/main/${message.uniqueId}" method="post">
                        <input type="hidden" name="_method" value="delete" />
                        <button type="submit">Удалить сообщение</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</@c.page>