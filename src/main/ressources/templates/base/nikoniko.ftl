<!DOCTYPE html>
<html>
<head>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

</head>

<body>
    <h1>${page}</h1>
    <table class="table table-bordered table-hover">
        <#list sortedFields as field>
            <#if field != "id">
                <#list item?keys as key>
                    <#if key == field>
                        <tr>
                            <th>${key}</th>
                            <#if item[key]?is_boolean>
                                <td>
                                    ${item[key]?c}
                                </td>
                            <#else>
                                <td>
                                    ${item[key]}
                                </td>
                            </#if>
                        </tr>
                    </#if>
                </#list>
            </#if>
        </#list>
    </table>

    <br>
        <a href="../index">Back</a>
    </br>
</body>
</html>