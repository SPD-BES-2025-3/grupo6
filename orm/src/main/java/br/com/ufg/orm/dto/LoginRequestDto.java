package br.com.ufg.orm.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String login;
    private String senha;
}
