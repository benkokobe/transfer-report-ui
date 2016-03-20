<%@include file="includes/header.jsp"%>


<form:form  method="post" modelAttribute="deploymentRequest">
 <li>
     <a>Enter the DR to generate release content</a>
  </li>
<div class="form-group">
    <label for="drName">Enter DR Name:</label>
    <form:input path="drName" class="form-control" placeholder="Enter DR name here"/>
</div>

<div class="form-group">
  <button type="submit" class="btn btn-default">Submit</button>
  </div>
</form:form>
<%@include file="includes/footer.jsp"%>