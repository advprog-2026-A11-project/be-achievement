# EC2 Provisioning for be-achievement

This folder provisions AWS infrastructure for the Achievement backend using Terraform.

It can create:
- an EC2 instance for `be-achievement`
- a security group with configurable open TCP ports
- an optional Elastic IP

## Prerequisites

- Terraform `>= 1.5`
- AWS credentials with EC2, VPC, security group, and Elastic IP permissions

## Configure

Copy the example environment file:

```bash
cp provisioning/.env.example provisioning/.env
```

Load the environment variables before running Terraform.

PowerShell:

```powershell
Get-Content provisioning/.env | ForEach-Object {
  if ($_ -match '^\s*#' -or $_ -notmatch '=') { return }
  $name, $value = $_ -split '=', 2
  Set-Item -Path "Env:$name" -Value $value
}
```

Bash:

```bash
set -a
source provisioning/.env
set +a
```

## Create Infrastructure

```bash
cd provisioning
terraform init
terraform plan -out tfplan
terraform apply tfplan
```

## Destroy Infrastructure

```bash
cd provisioning
terraform destroy
```

## Notes

- Default open ports are `22,8082`.
- If `TF_VAR_existing_security_group_id` is empty, Terraform creates a new security group.
- If `TF_VAR_existing_eip_allocation_id` is empty and `TF_VAR_assign_eip=true`, Terraform allocates a new Elastic IP.
- Set `TF_VAR_subnet_id` explicitly if the default VPC has multiple subnets and you need a specific one.
