import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Header from '../../components/header/Header';
import Cookies from 'js-cookie';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCheck, faEnvelope, faX, faUpload, faPaperPlane, faEye, faUsers, faFileImport } from '@fortawesome/free-solid-svg-icons';
import "../SendEmail/SendEmail.css"
function SendEmail() {
    // State for UI transitions
    const [isVisible, setIsVisible] = useState(false);
    
    // Form state
    const [templateType, setTemplateType] = useState("ALERT");
    const [error, setError] = useState<string | null>(null);
    const [fileName, setFileName] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);
    const [checkboxHabilitado, setCheckboxHabilitado] = useState<boolean>(false);
    const [loading, setLoading] = useState<boolean>(false);
    const [uploadResponse, setUploadResponse] = useState<any[] | null>(null);
    const [formData, setFormData] = useState({
        from: '',
        subject: '',
        pOne: '',
        pTwo: '',
        urlButton: '',
        urlBanner: '',
        type: 'ALERT',
        clients: [] as any[]
    });
    
    // Preview toggle
    const [showPreview, setShowPreview] = useState(true);

    useEffect(() => {
        // Set up timers for alerts
        const errorTimeout = setTimeout(() => {
            if (error) setError(null);
        }, 3000);

        const successTimeout = setTimeout(() => {
            if (success) setSuccess(null);
        }, 3000);

        // Animation delay
        setTimeout(() => {
            setIsVisible(true);
        }, 100);

        return () => {
            clearTimeout(errorTimeout);
            clearTimeout(successTimeout);
        };
    }, [error, success]);

    // Handle form input changes
    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setFormData((prevState) => ({
            ...prevState,
            [name]: value,
        }));

        if (name === "type") {
            setTemplateType(value);
        }
    };

    // Handle CSV file upload
    const handleUploadCsv = async (e: React.FormEvent) => {
        e.preventDefault();
        const token = Cookies.get("accessToken");

        if (!token) {
            setError("Usuário não autenticado. Faça login novamente.");
            return;
        }

        const fileInput = document.getElementById("csvFileInput") as HTMLInputElement;
        const file = fileInput?.files?.[0];

        if (!file) {
            setError("Nenhum arquivo CSV selecionado.");
            return;
        }

        setFileName(file.name);
        setLoading(true);

        const formData = new FormData();
        formData.append("file", file);

        try {
            const response = await axios.post(
                "http://localhost:8080/api/v1/send/email/upload/csv",
                formData,
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "multipart/form-data",
                    },
                }
            );

            setUploadResponse(response.data);
            fileInput.value = "";
            setSuccess("Arquivo CSV carregado com sucesso!");
        } catch (err: any) {
            console.error("Erro ao enviar arquivo CSV:", err.response?.data || err.message);
            setError(`Falha ao enviar arquivo CSV: ${err.response?.data?.message || err.message}`);
        } finally {
            setLoading(false);
        }
    };

    // Handle email send
    const handleSendEmail = async (e: React.FormEvent) => {
        e.preventDefault();
        const token = Cookies.get('accessToken');

        if (!token) {
            setError("Usuário não autenticado. Faça login novamente.");
            return;
        }
        
        if (!formData.from || !formData.subject || !formData.pOne || !formData.pTwo) {
            setError("Por favor, preencha todos os campos obrigatórios.");
            return;
        }

        setLoading(true);

        try {
            let updatedClients: any[] = [];

            if (checkboxHabilitado) {
                const clientsResponse = await axios.get(
                    "http://localhost:8080/api/v1/clients/get/all/no-pageable", 
                    {
                        headers: {
                            Authorization: `Bearer ${token}`,
                        }
                    }
                );
                updatedClients = Array.isArray(clientsResponse.data) ? clientsResponse.data : [];
            } else {
                updatedClients = Array.isArray(uploadResponse) ? uploadResponse : [];
                
                if (updatedClients.length === 0) {
                    setError("Nenhum cliente selecionado. Faça upload de um arquivo CSV ou selecione todos os clientes.");
                    setLoading(false);
                    return;
                }
            }

            await axios.post(
                "http://localhost:8080/api/v1/send/email",
                { ...formData, clients: updatedClients },
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "application/json"
                    }
                }
            );

            setSuccess("Emails enviados com sucesso!");

            setTimeout(() => {
                window.location.reload();
            }, 2000);

        } catch (err) {
            console.error("Erro ao enviar e-mail:", err);
            setError("Falha ao enviar e-mail. Verifique suas informações e tente novamente.");
        } finally {
            setLoading(false);
        }
    };

    // Get template badge class based on type
    const getBadgeClass = (type: string) => {
        switch(type) {
            case 'ALERT': return 'badge-alert';
            case 'PROMOTION': return 'badge-promotion';
            case 'CHARGE': return 'badge-charge';
            default: return 'badge-alert';
        }
    };

    // Get template name based on type
    const getTemplateName = (type: string) => {
        switch(type) {
            case 'ALERT': return 'Alerta';
            case 'PROMOTION': return 'Promoção';
            case 'CHARGE': return 'Cobrança';
            default: return 'Alerta';
        }
    };

    // Get button color based on template type
    const getButtonColor = (type: string) => {
        switch(type) {
            case 'ALERT': return '#ffd30d';
            case 'PROMOTION': return '#00c6a9';
            case 'CHARGE': return '#44a0ce';
            default: return '#ffd30d';
        }
    };

    // Get header color based on template type
    const getHeaderColor = (type: string) => {
        switch(type) {
            case 'ALERT': return '#ffd30d';
            case 'PROMOTION': return '#333333';
            case 'CHARGE': return '#44a0ce';
            default: return '#ffd30d';
        }
    };

    // Get button text based on template type
    const getButtonText = (type: string) => {
        switch(type) {
            case 'ALERT': return 'Ver Agora!';
            case 'PROMOTION': return 'Acessar Agora!';
            case 'CHARGE': return 'Pagar Agora!';
            default: return 'Ver Agora!';
        }
    };

    // Get text color for header based on template type
    const getHeaderTextColor = (type: string) => {
        switch(type) {
            case 'ALERT': return '#1f1b1b';
            case 'PROMOTION': return '#ffffff';
            case 'CHARGE': return '#ffffff';
            default: return '#1f1b1b';
        }
    };

    // Get button text color based on template type
    const getButtonTextColor = (type: string) => {
        switch(type) {
            case 'ALERT': return '#202020';
            case 'PROMOTION': return '#ffffff';
            case 'CHARGE': return '#ffffff';
            default: return '#202020';
        }
    };

    // Get icon URL based on template type
    const getIconUrl = (type: string) => {
        switch(type) {
            case 'ALERT': return "https://img.icons8.com/?size=100&id=8122&format=png&color=1f1b1b";
            case 'PROMOTION': return "https://img.icons8.com/?size=100&id=12095&format=png&color=FFFFFF";
            case 'CHARGE': return "https://img.icons8.com/?size=100&id=123507&format=png&color=FFFFFF";
            default: return "https://img.icons8.com/?size=100&id=8122&format=png&color=1f1b1b";
        }
    };

    return (
        <div className='container-send-email-principal'>
            {/* Notifications */}
            {error && <div className='alert alert-danger'><FontAwesomeIcon icon={faX} /> {error}</div>}
            {success && <div className='alert alert-success'><FontAwesomeIcon icon={faCheck} /> {success}</div>}
            
            {/* Header */}
            <div style={{ position: "absolute", top: "5px", left: "5px" }}><Header /></div>

            <div className='container-send-email'>
                {/* Form Section */}
                <div className='form-send-email'>
                    <h1 className='h1-send-email'>Envio de Emails</h1>
                    
                    <form onSubmit={handleSendEmail}>
                        <div className="input-group mb-3">
                            <span className="input-group-text">Conta Smtp</span>
                            <input
                                type="text"
                                name="from"
                                value={formData.from}
                                onChange={handleChange}
                                className="form-control"
                                placeholder="exemplo@empresa.com"
                                aria-label="Conta Smtp"
                            />
                        </div>

                        <div className="input-group mb-3">
                            <span className="input-group-text">Título</span>
                            <input
                                type="text"
                                name="subject"
                                value={formData.subject}
                                onChange={handleChange}
                                className="form-control"
                                placeholder="Título do email"
                                aria-label="Título"
                            />
                        </div>
                        
                        <div className="input-group mb-3">
                            <span className="input-group-text">Texto Primário</span>
                            <input
                                type="text"
                                name="pOne"
                                value={formData.pOne}
                                onChange={handleChange}
                                className="form-control"
                                placeholder="Texto principal do email"
                                aria-label="Texto Primário"
                            />
                        </div>
                        
                        <div className="input-group mb-3">
                            <span className="input-group-text">Texto Secundário</span>
                            <input
                                type="text"
                                name="pTwo"
                                value={formData.pTwo}
                                onChange={handleChange}
                                className="form-control"
                                placeholder="Texto complementar"
                                aria-label="Texto Secundário"
                            />
                        </div>
                        
                        <div className="input-group mb-3">
                            <span className="input-group-text">URL Botão</span>
                            <input
                                type="text"
                                name="urlButton"
                                value={formData.urlButton}
                                onChange={handleChange}
                                className="form-control"
                                placeholder="https://exemplo.com/acao"
                                aria-label="URL Botão"
                            />
                        </div>
                        
                        <div className="input-group mb-3">
                            <span className="input-group-text">URL Imagem</span>
                            <input
                                type="text"
                                name="urlBanner"
                                value={formData.urlBanner}
                                onChange={handleChange}
                                className="form-control"
                                placeholder="https://exemplo.com/imagem.jpg"
                                aria-label="URL Imagem"
                            />
                        </div>
                        
                        <select
                            name="type"
                            value={formData.type}
                            onChange={handleChange}
                            className="form-select"
                            aria-label="Selecione o Template"
                        >
                            <option value="" disabled>Selecione o Template</option>
                            <option value="ALERT">Alerta</option>
                            <option value="PROMOTION">Promoção</option>
                            <option value="CHARGE">Cobrança</option>
                        </select>
                        
                        <div className="form-check form-switch mb-3">
                            <input
                                className="form-check-input"
                                type="checkbox"
                                role="switch"
                                id="flexSwitchCheckChecked"
                                onChange={(e) => setCheckboxHabilitado(e.target.checked)}
                            />
                            <label className="form-check-label" htmlFor="flexSwitchCheckChecked">
                                Enviar para todos os Clientes <FontAwesomeIcon icon={faUsers} size="sm" />
                            </label>
                        </div>

                        {/* Preview Toggle */}
                        <div className="form-check form-switch mb-3">
                            <input
                                className="form-check-input"
                                type="checkbox"
                                role="switch"
                                id="togglePreview"
                                checked={showPreview}
                                onChange={(e) => setShowPreview(e.target.checked)}
                            />
                            <label className="form-check-label" htmlFor="togglePreview">
                                Mostrar Preview <FontAwesomeIcon icon={faEye} size="sm" />
                            </label>
                        </div>

                        <button 
                            type="submit" 
                            className='btn btn-primary' 
                            style={{ width: "100%" }}
                            disabled={loading}
                        >
                            <FontAwesomeIcon icon={faPaperPlane} /> 
                            {loading ? ' Enviando...' : ' Enviar Emails'}
                            {loading && 
                                <div className="spinner-grow ms-2" role="status">
                                    <span className="visually-hidden">Loading...</span>
                                </div>
                            }
                        </button>
                    </form>
                    
                    <hr />
                    
                    {/* CSV Upload Section */}
                    <form onSubmit={handleUploadCsv} style={{ width: "100%", margin: "auto" }}>
                        <h5 className="text-center mb-3">Upload de Clientes <FontAwesomeIcon icon={faFileImport} /></h5>
                        
                        <label className="custum-file-upload" htmlFor="csvFileInput">
                            <div className="text">
                                <img src="https://cdn3.iconfinder.com/data/icons/ikooni-outline-file-formats/128/files2-19-512.png" alt="" width="15%" />
                                <span>Clique para selecionar um arquivo CSV</span>
                            </div>
                            <input
                                type="file"
                                id="csvFileInput"
                                name="file"
                                accept=".csv"
                                required
                                onChange={(e) => {
                                    const file = e.target.files?.[0];
                                    if (file) {
                                        setFileName(file.name);
                                    }
                                }}
                            />
                        </label>
                        
                        {fileName && 
                            <div className="alert alert-info mt-2">
                                Arquivo selecionado: <strong>{fileName}</strong>
                            </div>
                        }
                        
                        <button 
                            type="submit" 
                            className='btn btn-add-client'
                            style={{ width: "100%" }}
                            disabled={loading}
                        >
                            <FontAwesomeIcon icon={faUpload} /> 
                            {loading ? ' Carregando...' : ' Fazer Upload do arquivo CSV'}
                            {loading && 
                                <div className="spinner-grow ms-2" role="status">
                                    <span className="visually-hidden">Loading...</span>
                                </div>
                            }
                        </button>
                    </form>
                </div>

                {/* Template Preview Section */}
                {showPreview && (
                    <div className={`template-send-email ${isVisible ? 'fade-in' : ''}`}>
                        <div className="preview-header">
                            <h4>Preview do Email</h4>
                            <span className={`template-type-badge ${getBadgeClass(templateType)}`}>
                                {getTemplateName(templateType)}
                            </span>
                        </div>
                        
                        <div className="email-template-preview">
                            <table width="100%" cellSpacing={0} cellPadding={0} style={{ borderCollapse: 'collapse' }}>
                                <tbody>
                                    <tr>
                                        <td align="center">
                                            <table width="100%" cellSpacing={0} cellPadding={0} style={{ backgroundColor: '#ffffff', border: '1px solid #e0e5ec', borderRadius: '8px', overflow: 'hidden' }}>
                                                {/* Header */}
                                                <tr style={{ backgroundColor: getHeaderColor(templateType) }}>
                                                    <td align="center" style={{ padding: '15px', color: getHeaderTextColor(templateType), fontFamily: 'Arial, sans-serif' }}>
                                                        <img src={getIconUrl(templateType)} width={32} height={32} alt="Icon" />
                                                        <h2 style={{ margin: '10px 0', wordBreak: "break-word", fontSize: "1.2em", color: getHeaderTextColor(templateType) }}>
                                                            {formData.subject || "Título do Email"}
                                                        </h2>
                                                    </td>
                                                </tr>
                                                
                                                {/* Content */}
                                                <tr>
                                                    <td style={{ padding: '20px', fontFamily: 'Arial, sans-serif', color: '#333' }}>
                                                        <p style={{ fontSize: '1em' }}>
                                                            Olá <strong>Usuário</strong>,
                                                            {templateType === 'ALERT' && ' leia esse email com atenção!'}
                                                            {templateType === 'PROMOTION' && ' confira as promoções!'}
                                                            {templateType === 'CHARGE' && ' temos um aviso importante!'}
                                                        </p>
                                                        
                                                        <hr style={{ backgroundColor: '#e0e5ec', height: '1px', border: 'none' }} />
                                                        
                                                        <p style={{ color: '#505050', padding: '5px', wordBreak: "break-word" }}>
                                                            {formData.pOne || "Texto principal do email"}
                                                        </p>
                                                        
                                                        <hr style={{ width: '80px', backgroundColor: '#e0e5ec', border: 'none', height: '1px', margin: '15px auto' }} />
                                                        
                                                        <p style={{ color: '#505050', padding: '5px', wordBreak: "break-word" }}>
                                                            {formData.pTwo || "Texto complementar do email"}
                                                        </p>

                                                        <img
                                                            src={formData.urlBanner || "https://i0.wp.com/espaferro.com.br/wp-content/uploads/2024/06/placeholder-103.png?ssl=1"}
                                                            alt="Banner"
                                                            style={{ width: '80%', borderRadius: '8px', margin: '15px auto', display: 'block' }}
                                                        />
                                                        
                                                        <hr style={{ backgroundColor: '#e0e5ec', height: '1px', border: 'none', margin: '15px 0' }} />
                                                        
                                                        <p style={{ fontSize: '0.9em', color: '#666' }}>
                                                            Se você tiver dúvidas, visite nossa <a href="#" style={{ color: '#4361ee', textDecoration: 'none' }}>Central de Ajuda</a>.
                                                        </p>
                                                        
                                                        <p style={{ fontSize: '0.9em', color: '#666' }}>
                                                            Atenciosamente,<br />
                                                            <strong>Nome da Empresa</strong>
                                                        </p>
                                                    </td>
                                                </tr>
                                                
                                                {/* Button */}
                                                <tr>
                                                    <td align="center" style={{ padding: '10px 10px 20px' }}>
                                                        <a
                                                            href={formData.urlButton || "#"}
                                                            style={{
                                                                backgroundColor: getButtonColor(templateType),
                                                                color: getButtonTextColor(templateType),
                                                                padding: '12px 24px',
                                                                textDecoration: 'none',
                                                                fontSize: '14px',
                                                                fontFamily: 'Arial, sans-serif',
                                                                borderRadius: '6px',
                                                                fontWeight: '500',
                                                                display: 'inline-block'
                                                            }}
                                                        >
                                                            {getButtonText(templateType)}
                                                        </a>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
}

export default SendEmail;