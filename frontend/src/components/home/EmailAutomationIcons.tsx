import React from 'react';

const EmailAutomationIcons = () => {
  const iconStyle = {
    display: 'flex',
    flexDirection: 'column' as 'column',  // Especificar o tipo correto
    alignItems: 'center',
    justifyContent: 'center',
    width: '120px',
    textAlign: 'center' as 'center' // Especificar o tipo correto
  };

  const circleStyle = {
    width: '70px',
    height: '70px',
    backgroundColor: '#f5f5f5',
    borderRadius: '50%',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: '10px'
  };

  const containerStyle = {
    display: 'flex',
    justifyContent: 'space-around',
    padding: '30px',
    backgroundColor: 'white',
    borderRadius: '8px',
    maxWidth: '800px',
    margin: '0 auto'
  };

  const titleStyle = {
    fontSize: '12px',
    fontWeight: 'bold',
    textTransform: 'uppercase' as 'uppercase', // Especificar o tipo correto
    color: '#555',
    marginTop: '5px',
    lineHeight: '1.2'
  };

  const subtitleStyle = {
    fontSize: '10px',
    color: '#888',
    marginTop: '3px',
    textTransform: 'uppercase' as 'uppercase', // Especificar o tipo correto
    lineHeight: '1.2'
  };

  return (
    <div style={containerStyle}>
      <div style={iconStyle}>
        <div style={circleStyle}>
          <svg width="30" height="30" viewBox="0 0 24 24" fill="none" stroke="#333" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
            <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/>
          </svg>
        </div>
        <div style={titleStyle}>Automação de</div>
        <div style={subtitleStyle}>Boas-vindas</div>
      </div>

      <div style={iconStyle}>
        <div style={circleStyle}>
          <svg width="30" height="30" viewBox="0 0 24 24" fill="none" stroke="#333" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
            <rect x="3" y="3" width="18" height="18" rx="2" ry="2"/>
            <line x1="3" y1="9" x2="21" y2="9"/>
            <line x1="9" y1="21" x2="9" y2="9"/>
          </svg>
        </div>
        <div style={titleStyle}>Sequência de</div>
        <div style={subtitleStyle}>Emails</div>
      </div>

      <div style={iconStyle}>
        <div style={circleStyle}>
          <svg width="30" height="30" viewBox="0 0 24 24" fill="none" stroke="#333" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
            <circle cx="12" cy="12" r="10"/>
            <polyline points="12 6 12 12 16 14"/>
          </svg>
        </div>
        <div style={titleStyle}>Gatilhos</div>
        <div style={subtitleStyle}>Inteligentes</div>
      </div>

      <div style={iconStyle}>
        <div style={circleStyle}>
          <svg width="30" height="30" viewBox="0 0 24 24" fill="none" stroke="#333" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
            <path d="M22 12h-4l-3 9L9 3l-3 9H2"/>
          </svg>
        </div>
        <div style={titleStyle}>Análise de</div>
        <div style={subtitleStyle}>Desempenho</div>
      </div>
    </div>
  );
};

export default EmailAutomationIcons;
