import React, { useState, useEffect } from 'react';
import logo from '../../assets/images/logos/Logo.png';
import { useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { setCookie } from 'nookies';
import { useAuth } from "../../context/AuthContext";
import axios from "axios";
import { faCircleCheck, faCircleXmark, faEnvelope, faLock, faEye, faEyeSlash } from '@fortawesome/free-solid-svg-icons';

function Login() {
    const { login } = useAuth();
    const navigate = useNavigate();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);
    const [loading, setLoading] = useState(false);
    const [showPassword, setShowPassword] = useState(false);
    const [rememberMe, setRememberMe] = useState(false);

    // Recuperar email salvo se existir
    useEffect(() => {
        const savedEmail = localStorage.getItem('rememberedEmail');
        if (savedEmail) {
            setEmail(savedEmail);
            setRememberMe(true);
        }
    }, []);

    const handleLogin = async (e) => {
        e.preventDefault();
    
        if (!email || !password) {
            setError("Por favor, preencha todos os campos.");
            return;
        }
    
        setLoading(true);
        setError(null);
    
        try {
            const response = await axios.post("http://localhost:8080/api/v1/auth/login", {
                email,
                password,
            });
    
            const { accessToken, refreshToken } = response.data;
    
            // Salvar email se "lembrar-me" estiver marcado
            if (rememberMe) {
                localStorage.setItem('rememberedEmail', email);
            } else {
                localStorage.removeItem('rememberedEmail');
            }
    
            // Chama a função `login` para salvar apenas o accessToken
            login(accessToken, refreshToken);


            
    
            setSuccess("Login bem sucedido!");
    
            // Redireciona após 1 segundo
            setTimeout(() => {
                navigate("/dashboard");
            }, 1000);
    
        } catch (err) {
            console.error("Erro no login:", err);
            setError("Credenciais inválidas. Verifique seu email e senha.");
        } finally {
            setLoading(false);
        }
    };
    
    

    const togglePasswordVisibility = () => {
        setShowPassword(!showPassword);
    };

    const handleForgotPassword = () => {
        navigate("/recuperar-senha");
    };

    return (
        <div className="container-principal-login">

            <div className="container-login">

                <div className="login-form-container">
                    <form className="form_container" onSubmit={handleLogin}>
                        <div className="logo_container">
                            <img src={logo} alt="Logo da empresa" className="logo-image" />
                        </div>
                        {error && (
                            <div className='notification error-message'>
                                <FontAwesomeIcon icon={faCircleXmark} className='notification-icon' />
                                <span>{error}</span>
                            </div>
                        )}

                        {success && (
                            <div className='notification success-message'>
                                <FontAwesomeIcon icon={faCircleCheck} className='notification-icon' />
                                <span>{success}</span>
                            </div>
                        )}
                        <div className="title_container">
                            <h1 className="title">Bem-vindo de volta</h1>
                            <p className="subtitle">
                                Entre na sua conta para acessar todas as funcionalidades
                            </p>
                        </div>



                        <div className="input_container">
                            <label className="input_label" htmlFor="email_field">
                                Email
                            </label>
                            <div className="input-with-icon">
                                <FontAwesomeIcon icon={faEnvelope} className="input-icon" />
                                <input
                                    placeholder="seu@email.com"
                                    name="email"
                                    type="email"
                                    className="input_field"
                                    id="email_field"
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                    autoComplete="username"
                                />
                            </div>
                        </div>

                        <div className="input_container">
                            <label className="input_label" htmlFor="password_field">
                                Senha
                            </label>
                            <div className="input-with-icon">
                                <FontAwesomeIcon icon={faLock} className="input-icon" />
                                <input
                                    placeholder="Sua senha"
                                    name="password"
                                    type={showPassword ? "text" : "password"}
                                    className="input_field"
                                    id="password_field"
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                    autoComplete="current-password"
                                />
                                <button
                                    type="button"
                                    className="password-toggle"
                                    onClick={togglePasswordVisibility}
                                    aria-label={showPassword ? "Esconder senha" : "Mostrar senha"}
                                >
                                    <FontAwesomeIcon icon={showPassword ? faEyeSlash : faEye} />
                                </button>
                            </div>
                        </div>

                        <div className="login-options">
                            <div className="remember-me">
                                <input
                                    type="checkbox"
                                    id="remember-me"
                                    checked={rememberMe}
                                    onChange={() => setRememberMe(!rememberMe)}
                                />
                                <label htmlFor="remember-me">Lembrar-me</label>
                            </div>
                            <button
                                type="button"
                                className="forgot-password"
                                onClick={handleForgotPassword}
                            >
                                Esqueceu a senha?
                            </button>
                        </div>

                        <button
                            title="Entrar"
                            type="submit"
                            className="sign-in_btn"
                            disabled={loading}
                        >
                            {loading ? (
                                <span className="loading-spinner"></span>
                            ) : (
                                <span>Entrar</span>
                            )}
                        </button>

                        <div className="separator">
                            <hr className="line" />
                            <span>Ou continue com</span>
                            <hr className="line" />
                        </div>


                        <p className="register-link">
                            Não tem uma conta? <a href="/registrar">Cadastre-se aqui</a>
                        </p>
                    </form>
                </div>

                <div className="img-banner-login">
                    <div className="banner-content">
                        <h2>Automatize seus emails</h2>
                        <p>
                            Transforme sua comunicação profissional e economize tempo com nossa
                            plataforma de automação de emails.
                        </p>
                        <ul className="feature-list">
                            <li>Aumente sua produtividade</li>
                            <li>Templates profissionais</li>
                            <li>Métricas detalhadas</li>
                            <li>Integração com diversas plataformas</li>
                        </ul>
                        <a href="/registrar">
                            <button className="btn-register-account">
                                Criar Conta Gratuita
                            </button>
                        </a>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Login;