package br.ufpb.dsc.nexushub.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordWithTokenRequest(
    @NotBlank(message = "Token é obrigatório.")
    String token,

    @NotBlank(message = "Nova senha é obrigatória.")
    @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres.")
    String novaSenha
) {}
