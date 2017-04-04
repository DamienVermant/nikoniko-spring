<head>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

</head>

<body>
	<a href="/menu">Home</a>
    <h1>${page}</h1>

        <table class="table table-bordered table-hover">
            <tr>
                <#list items as item>
                    <#list sortedFields as field>
                        <#list item?keys as key>
                            <#if key == field>
                                <th>${key}</th>
                            </#if>
                        </#list>
                    </#list>
                    <#break>
                </#list>
            </tr>
            <#list items as item>
                <tr>
                    <#list sortedFields as field>
                        <#list item?keys as key>
                            <#if key == field>
                                <#if item[key]?is_boolean>
                                    <td>${item[key]?c}</td>
                                <#elseif item[key]?is_date_like>
                                    <td>${item[key]?string("yyyy:MM:dd HH:mm:ss")}</td>
                                <#else>
                                    <td>${item[key]}</td>
                                </#if>
                            </#if>
                        </#list>
                    </#list>
                    <td>
                    	<a href="/${"nikoniko"}/${item["id"]}/${"show"}">Select</a>
                    </td>
                </tr>
            </#list>
        </table>
<a href = "${back}"> Back <a>
</body>