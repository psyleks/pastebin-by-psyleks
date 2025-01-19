<#import "parts/common.ftlh" as c>

<@c.page>

    <form method="get" action="/main" class="d-flex justify-content-end">
        <div class="input-group mb-3 col-md-6 col-lg-4 " style="max-width: 300px; width: 100%;">
            <input type="text" name="filter" class="form-control" value="${filter!}" placeholder="Search by tag"
                   aria-describedby="button-addon2">
            <button type="submit" class="btn btn-outline-primary" id="button-addon2">Search</button>
        </div>
    </form>

    <form method="post" enctype="multipart/form-data" class="d-flex justify-content-center">
        <div class="card card-body my-1 col-md-6" style="max-width: 40vw;">

            <div>
                <input type="text" class="form-control mt-2 ${(tagError??)?string('is-invalid', '')}"
                       value="<#if message??> ${message.tag} </#if>" name="tag" placeholder="Tag">
                <#if tagError??>
                    <div class="invalid-feedback">
                        ${tagError}
                    </div>
                </#if>
            </div>

            <input type="text" class="form-control mt-2 ${(textError??)?string('is-invalid', '')} "
                   value="<#if message??> ${message.text} </#if>" name="text" placeholder="Enter message"/>
            <#if textError??>
                <div class="invalid-feedback">
                    ${textError}
                </div>
            </#if>

            <div class="mt-2">
                <input class="form-control" type="file" name="file" id="formFile">
                <label for="formFile" class="form-label"></label>
            </div>
            <#if sizeError??>
                <div class="invalid-feedback">
                    ${sizeError}
                </div>
            </#if>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <div class="d-flex justify-content-end">
                <button type="submit" class="btn btn-primary"> Post</button>
            </div>
        </div>
    </form>

    <div class="row">
        <#list messages as message>
            <div class="card my-3">
                <a href="/main/${message.uniqueId}" class="card-link">
                    <div class="row g-0">
                        <#if message.filename??>
                            <div class="col-md-4">
                                <img src="/uploads/${message.filename}" class="img-fluid rounded-start" alt="...">
                            </div>
                            <div class="col-md-8">
                                <div class="card-body">
                                    <h4 class="card-text">${message.authorName}</h4>
                                    <small class="text-muted">${formattedDates[message_index]}</small>
                                    <hr style="margin-top: 10px; margin-bottom: 10px;">
                                    <h5 class="card-title">${message.tag}</h5>
                                    <p class="card-text">${message.text}</p>
                                </div>
                            </div>
                        </#if>
                        <#if !message.filename??>
                            <div class="col-md-12">
                                <div class="card-body">
                                    <h4 class="card-text">${message.authorName}</h4>
                                    <small class="text-muted">${formattedDates[message_index]}</small>
                                    <hr style="margin-top: 10px; margin-bottom: 10px;">
                                    <h5 class="card-title">${message.tag}</h5>
                                    <p class="card-text">${message.text}</p>
                                </div>
                            </div>
                        </#if>
                    </div>
                </a>
            </div>
        <#else>
            <div class="mt-5">No messages</div>
        </#list>
    </div>

</@c.page>




