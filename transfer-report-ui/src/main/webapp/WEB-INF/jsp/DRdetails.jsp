<%@include file="includes/header.jsp"%>	

       <div class="row">
            <div class="col-lg-6">
                <div class="well text-left">
                    <p>Below is a summary of the deployment request.
                        <h3>Click <a href="${deploymentRequest.drName}.xls?drName=${deploymentRequest.drName}">here</a> to generate an excel report.
                        </h3>
                    </p>
                </div>
            </div>
            <!-- /.col-lg-12 -->
        </div>
	<tr>
	  <td>
		<table class = "table">
			<tr>
				<td align="left">DR name: ${deploymentRequest.drName}</td>
			</tr>
			<tr>
				<td align="left">DR synopsis: ${deploymentRequest.synopsis}</td>
			</tr>
		</table>
	  </td>
	</tr>
	<tr><td>
		<table class = "table">
			<thead>
				<tr>
					<td>DR details:</td>
				</tr>
				<tr>
					<td>Nr. of patches</td>
					<td>Nr. of transfer operation</td>
					<td>Nr. of manual operations</td>
					<td>Nr. of subjects</td>
				</tr>
				<tr>
				    <td>${deploymentRequest.numberOfPatches}</td>
					<td>${deploymentRequest.numberOfTransferOperations}</td>
					<td>${deploymentRequest.numberOfManualTransferOperations}</td>
					<td>${deploymentRequest.numberOfSubjects}</td>
				</tr>
			</thead>

		</table>
	</td></tr>
</table>
<%@include file="includes/footer.jsp"%>
