<#import "parts/common.ftlh" as c>


<@c.page>
    <h5>${username!}</h5>
    <div class="alert alert-danger" role="alert">
        ${message!}
    </div>
    <form method="post">
        <div class="form-group row mt-2">
            <label class="col-sm-2 col-form-label">Old password:</label>
            <div class="col-sm-6">
                <input type="password" name="passwordConfirm" class="form-control" placeholder="Password"/>
            </div>
        </div>
        <div class="form-group row mt-2">
            <label class="col-sm-2 col-form-label">New password:</label>
            <div class="col-sm-6">
                <input type="password" name="password" class="form-control" placeholder="Password"/>
            </div>
        </div>
        <div class="form-group row mt-2">
            <label class="col-sm-2 col-form-label">Email:</label>
            <div class="col-sm-6">
                <input type="email" name="email" class="form-control" placeholder="example@mail.com"
                       value="${email!''}"/>
            </div>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <button class="btn btn-primary mt-2" type="submit">Save</button>
    </form>
</@c.page>