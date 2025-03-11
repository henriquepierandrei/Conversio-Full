import React, { useState } from 'react';
import Header from '../../components/header/Header';
import alertTemplate from '../../assets/images/templates/alertEmail.png';
import chargeTemplate from '../../assets/images/templates/chargeEmail.png';
import promotionTemplate from '../../assets/images/templates/promotionEmail.png';
import './Themes.css';

function Themes() {
  const [selectedTemplate, setSelectedTemplate] = useState(null);
  
  const templates = [
    {
      id: 'alert',
      title: 'Template Alerta',
      color: '#d4a704',
      image: alertTemplate,
      description: 'Este modelo será usado para enviar e-mails para alertar sobre perigos, golpes ou qualquer atividade maliciosa relacionada à sua empresa.'
    },
    {
      id: 'promotion',
      title: 'Template Oferta',
      color: '#e83b3b',
      image: promotionTemplate,
      description: 'Este modelo será usado para enviar e-mails para promover seus serviços ou produtos.'
    },
    {
      id: 'charge',
      title: 'Template Cobrança',
      color: '#4878d6',
      image: chargeTemplate,
      description: 'Este modelo será usado para enviar e-mails para aqueles que têm alguma dívida financeira com sua empresa.'
    }
  ];

  const handleSelectTemplate = (id) => {
    setSelectedTemplate(id === selectedTemplate ? null : id);
  };

  return (
    <div className="themes-container">
      <div className="themes-header">
        <Header />
      </div>
      
      <div className="themes-content">
        <br />
        
        <div className="templates-grid">
          {templates.map((template) => (
            <div 
              key={template.id}
              className={`template-card ${selectedTemplate === template.id ? 'selected' : ''}`}
              onClick={() => handleSelectTemplate(template.id)}
            >
              <div className="template-header" style={{ backgroundColor: template.color }}>
                <h3>{template.title}</h3>
                {selectedTemplate === template.id && (
                  <span className="selected-badge">Selecionado</span>
                )}
              </div>
              
              <div className="template-image-container">
                <img src={template.image} alt={template.title} className="template-image" />
              </div>
              
              <div className="template-description">
                <p>{template.description}</p>
              </div>
              
              <button 
                className="template-button"
                style={{ backgroundColor: template.color }}
              >
                Visualizar
              </button>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default Themes;