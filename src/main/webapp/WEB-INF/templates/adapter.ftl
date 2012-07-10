<html>
<body>
<h3>Versus > Adapters > ${adapter.name}</h3>
<ul>
  <li>Name: ${adapter.name}</li>
  <li>Supported types:</li>
  <ul>
    <#list adapter.supportedMediaTypes as x>
      <li>${x}</li>
    </#list> 
  </ul>
</ul>
</body>
</html>