output "instance_id" {
  description = "Achievement backend EC2 instance ID."
  value       = aws_instance.achievement.id
}

output "public_ip" {
  description = "Public IP for the achievement backend instance."
  value = (
    local.create_eip ? aws_eip.achievement[0].public_ip :
    local.use_existing_eip ? data.aws_eip.existing[0].public_ip :
    aws_instance.achievement.public_ip
  )
}

output "public_dns" {
  description = "Public DNS for the achievement backend instance."
  value       = aws_instance.achievement.public_dns
}

output "security_group_id" {
  description = "Security group attached to the achievement backend instance."
  value       = local.effective_security_group_id
}
