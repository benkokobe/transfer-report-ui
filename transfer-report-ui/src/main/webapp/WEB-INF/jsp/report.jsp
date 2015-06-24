<h1>Deployment Request details</h1>
<table>
	<tr><td>
		<table>
			<tr>
				<td>DR name:</td>
				<td>${deploymentRequest.drName}</td>
			</tr>
			<tr>
				<td>DR synopsis:</td>
				<td>${deploymentRequest.drName}</td>
			</tr>
		</table>
	</td></tr>
	<tr><td>
		<table>
			<thead>
				<tr>
					<td>DR details:</td>
				</tr>
				<tr>
					<td>Nr. of patches</td>
					<td>Nr. transfer operation</td>
					<td>Nr. manual operations</td>
				</tr>
				<tr>
				    <td>${deploymentRequest.numberOfPatches}</td>
					<td>${deploymentRequest.numberOfTransferOperations}</td>
					<td>${deploymentRequest.drName}</td>
				</tr>
			</thead>

		</table>
	</td></tr>
</table>
Click <a href="generate.xls?drName=${deploymentRequest.drName}">here</a> to generate an excel report.
</div>