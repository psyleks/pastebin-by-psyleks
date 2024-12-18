<#import "parts/common.ftlh" as c>

<@c.page>
    <div class="form-row">
        <div class="form-group col-md-6">
            <form method="get" action="/main" class="form-inline">
                <input type="text" name="filter" class="form-control" value="${filter!}" placeholder="Search by tag">
                <button type="submit" class="btn btn-primary mt-2">Search</button>
            </form>
        </div>
    </div>

    <p>
        <button class="btn btn-primary mt-2" type="button" data-bs-toggle="collapse" data-bs-target="#collapseExample"
                aria-expanded="false" aria-controls="collapseExample">
            Add new message
        </button>
    </p>
    <div class="collapse<#if message??>.show</#if>" id="collapseExample">
        <div class="card card-body my-3 col-md-6">
            <form method="post" enctype="multipart/form-data">

                <input type="text" class="form-control mt-2"
                       value="<#if message??> ${message.tag} </#if>" name="tag" placeholder="Tag">
                <#if tagError??>
                    <div class="invalid-feedback">
                        ${tagError}
                    </div>
                </#if>

                <input type="text" class="form-control mt-2 ${(textError??)?string('is-invalid', '')} "
                       value="<#if message??> ${message.text} </#if>" name="text" placeholder="Enter message"/>
                <#if textError??>
                    <div class="invalid-feedback">
                        ${textError}
                    </div>
                </#if>

                <div class="custom-file mt-2">
                    <input type="file" name="file" id="customFile">
                    <label class="custom-file-label" for="customFile"></label>
                </div>
                <#if sizeError??>
                    <div class="invalid-feedback">
                        ${sizeError}
                    </div>
                </#if>
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <div class="form-group mt-2">
                    <button type="submit" class="btn btn-primary">Post</button>
                </div>
            </form>
        </div>
    </div>

    <div class="row row-cols-1 row-cols-md-4 g-4">
        <#list messages as message>
            <div class="col">
                <a href="/main/${message.id}" class="card-link">
                    <div class="card" style="width: 18rem;">
                        <#if message.filename??>
                            <img src="/uploads/${message.filename}" class="card-img-top" alt="">
                        </#if>
                        <div class="card-body">
                            <h5>${message.tag}</h5>
                            <p class="card-text">${message.text}</p>
                        </div>
                        <div class="card-footer">
                            <small class="text-body-secondary"> ${message.authorName}</small>
                        </div>
                    </div>
                </a>
            </div>
        <#else>
            <div class="mt-5">No messages</div>
        </#list>
    </div>
</@c.page>