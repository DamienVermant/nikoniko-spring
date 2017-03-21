<head>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

</head>

<body>
    <h1>${page}</h1>
    <table class="table table-bordered table-hover">
        <#list currentItem?keys as key>
            <#if key != "id" && !currentItem[key]?is_sequence>
                <tr>
                    <th>${key}</th>
                    <#if currentItem[key]?is_boolean>
                        <td>${currentItem[key]?c}</td>
                    <#elseif currentItem[key]?is_date_like>
                        <td>${currentItem[key]?string("yyyy/MM/dd HH:mm:ss")}</td>
                    <#else>
                        <td>${currentItem[key]}</td>
                    </#if>
                </tr>
            </#if>
        </#list>
    </table>
         <#if items?has_content>
        <table class="table table-bordered table-hover">
            <tr>
                <#list items as item>
                    <#list fields as field>
                        <#list item?keys as key>
                            <#if field == key && key != "id">
                                <th>${key}</th>
                            </#if>
                        </#list>
                    </#list>
                    <#break>
                </#list>
            </tr>

                <#list items as item>
                    <tr>
                        <#list fields as field>
                            <#list item?keys as key>
                                <#if field == key>
                                    <#if key != "id">
                                        <#if item[key]?is_boolean>
                                            <td>${item[key]?c}</td>
                                        <#elseif item[key]?is_date_like>
                                            <td>${item[key]?string("yyyy/MM/dd HH:mm:ss")}</td>
                                        <#else>
                                            <td>${item[key]}</td>
                                        </#if>
                                    </#if>
                                </#if>
                            </#list>
                        </#list>
                    </tr>
                </#list>
        </table>
    <#else>
        <br>
            Empty association
        </br>
    </#if>
    <br>
        <a href="../index">Back</a>
    </br>
</body>