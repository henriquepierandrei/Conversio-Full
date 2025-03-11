import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "../auth/Auth.css";
import logo from "../../assets/images/logos/Logo.png";

function Register() {
    const [companyName, setCompanyName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);
    const [loading, setLoading] = useState(false);
    
    const navigate = useNavigate();

    const handleRegister = async (e) => {
        e.preventDefault();

        if (!email || !password || !companyName) {
            setError("Por favor, preencha todos os campos.");
            return;
        }

        setLoading(true);
        setError(null);

        try {
            const response = await axios.post("http://localhost:8080/api/v1/auth/register", {
                companyName,
                email,
                password,
            });

            setSuccess(response.data.message);

            // Redireciona após 1 segundo
            setTimeout(() => {
                navigate("/login");
            }, 1000);

        } catch (err) {
            console.error("Erro no registro:", err);
            setError("Erro ao registrar usuário. Verifique os dados inseridos.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="container-principal-login">
            <div className="container-login">
                <div className="login-form-container">
                    <form className="form_container" onSubmit={handleRegister}>
                        <div className="logo_container">
                            <img src={logo} alt="Logo" className="logo-image" />
                        </div>

                        <div className="title_container">
                            <p className="title">Criar uma Conta</p>
                            <span className="subtitle">
                                Comece agora a automatizar seus emails para ser mais produtivo e profissional!
                            </span>
                        </div>

                        {error && <p className="error-message">{error}</p>}
                        {success && <p className="success-message">{success}</p>}

                        <div className="input_container">
                            <label className="input_label" htmlFor="name_field">
                                <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                                    <path d="M20 7h-3a2 2 0 0 0-2 2v1H9V9a2 2 0 0 0-2-2H4a2 2 0 0 0-2 2v11h20V9a2 2 0 0 0-2-2Z"></path>
                                    <path d="M14 4h6v3h-6z"></path>
                                    <path d="M4 4h6v3H4z"></path>
                                </svg>
                                Nome da Empresa
                            </label>
                            <div className="input_field_container">
                                <svg className="input_icon" xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                                    <rect x="2" y="7" width="20" height="14" rx="2" ry="2"></rect>
                                    <path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"></path>
                                </svg>
                                <input
                                    id="name_field"
                                    type="text"
                                    className="input_field"
                                    placeholder="Digite seu nome"
                                    value={companyName}
                                    onChange={(e) => setCompanyName(e.target.value)}
                                />
                            </div>
                        </div>

                        <div className="input_container">
                            <label className="input_label" htmlFor="email_field">
                                <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                                    <rect x="2" y="4" width="20" height="16" rx="2"></rect>
                                    <path d="m22 7-8.97 5.7a1.94 1.94 0 0 1-2.06 0L2 7"></path>
                                </svg>
                                Email
                            </label>
                            <div className="input_field_container">
                                <svg className="input_icon" xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                                    <path d="M19 21v-2a4 4 0 0 0-4-4H9a4 4 0 0 0-4 4v2"></path>
                                    <circle cx="12" cy="7" r="4"></circle>
                                </svg>
                                <input
                                    id="email_field"
                                    type="email"
                                    className="input_field"
                                    placeholder="seu@email.com"
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                />
                            </div>
                        </div>

                        <div className="input_container">
                            <label className="input_label" htmlFor="password_field">
                                <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                                    <rect x="3" y="11" width="18" height="11" rx="2" ry="2"></rect>
                                    <path d="M7 11V7a5 5 0 0 1 10 0v4"></path>
                                </svg>
                                Senha
                            </label>
                            <div className="input_field_container">
                                <svg className="input_icon" xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                                    <rect x="3" y="11" width="18" height="11" rx="2" ry="2"></rect>
                                    <path d="M7 11V7a5 5 0 0 1 10 0v4"></path>
                                </svg>
                                <input
                                    id="password_field"
                                    type="password"
                                    className="input_field"
                                    placeholder="Digite sua senha"
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                />
                            </div>
                        </div>

                        <button type="submit" className="sign-in_btn" disabled={loading}>
                            {loading ? (
                                <>
                                    <svg className="loading-svg" xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                                        <path d="M21 12a9 9 0 1 1-6.219-8.56"></path>
                                    </svg>
                                    Criando Conta...
                                </>
                            ) : (
                                <>
                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                                        <path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"></path>
                                        <circle cx="9" cy="7" r="4"></circle>
                                        <line x1="19" y1="8" x2="19" y2="14"></line>
                                        <line x1="22" y1="11" x2="16" y2="11"></line>
                                    </svg>
                                    Criar Conta
                                </>
                            )}
                        </button>
                    </form>
                </div>

                <div className="img-banner-login">
                    <div className="banner-content">
                        <h2>Já tem uma conta?</h2>
                        <p>
                            Faça login agora e aumente a produtividade do seu trabalho com automação de emails!
                        </p>
                        <a href="/login">
                            <button className="btn-register-account">
                                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="mr-2">
                                    <path d="M15 3h4a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2h-4"></path>
                                    <polyline points="10 17 15 12 10 7"></polyline>
                                    <line x1="15" y1="12" x2="3" y2="12"></line>
                                </svg>
                                Login na Conta
                            </button>
                        </a>
                    </div>
                    
                </div>
            </div>
        </div>
    );
}

export default Register;