variable "aws_region" {
  description = "AWS region used for provisioning."
  type        = string
}

variable "aws_profile" {
  description = "Local AWS CLI profile. Leave empty in CI if credentials are provided by environment."
  type        = string
  default     = ""
}

variable "project_tag" {
  description = "Project tag applied to managed resources."
  type        = string
  default     = "yomu-achievement"
}

variable "env_tag" {
  description = "Environment tag applied to managed resources."
  type        = string
  default     = "staging"
}

variable "extra_tags" {
  description = "Additional resource tags."
  type        = map(string)
  default     = {}
}

variable "instance_name" {
  description = "EC2 instance name."
  type        = string
  default     = "yomu-achievement-staging"
}

variable "security_group_name" {
  description = "Security group name."
  type        = string
  default     = "yomu-achievement-sg"
}

variable "ami_id" {
  description = "AMI ID for the EC2 instance."
  type        = string
}

variable "instance_type" {
  description = "EC2 instance type."
  type        = string
  default     = "t3.micro"
}

variable "key_pair_name" {
  description = "Existing EC2 key pair name. Leave empty for no key pair."
  type        = string
  default     = ""
}

variable "root_volume_gb" {
  description = "Root EBS volume size in GB."
  type        = number
  default     = 16
}

variable "vpc_id" {
  description = "VPC ID. Leave empty to use the default VPC."
  type        = string
  default     = ""
}

variable "subnet_id" {
  description = "Subnet ID. Leave empty to use the first subnet in the selected VPC."
  type        = string
  default     = ""
}

variable "existing_security_group_id" {
  description = "Existing security group ID. Leave empty to create one."
  type        = string
  default     = ""
}

variable "open_ports_csv" {
  description = "Comma-separated TCP ports opened on the generated security group."
  type        = string
  default     = "22,8082"
}

variable "assign_eip" {
  description = "Whether to associate an Elastic IP with the EC2 instance."
  type        = bool
  default     = true
}

variable "existing_eip_allocation_id" {
  description = "Existing Elastic IP allocation ID. Leave empty to create a new EIP."
  type        = string
  default     = ""
}
