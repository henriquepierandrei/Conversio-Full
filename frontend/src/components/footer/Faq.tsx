import React, { useState } from 'react';
import './Faq.css';

const FaqItem = ({ question, answer, defaultOpen = false }) => {
  const [isOpen, setIsOpen] = useState(defaultOpen);

  return (
    <div className="faq-item">
      <button
        className="faq-question"
        onClick={() => setIsOpen(!isOpen)}
      >
        <span className="faq-question-text">{question}</span>
        <span className="faq-icon">
          {isOpen ? '−' : '+'}
        </span>
      </button>
      
      {isOpen && (
        <div className="faq-answer">
          <p>{answer}</p>
        </div>
      )}
    </div>
  );
};

const Faq = () => {
  const faqData = [
    {
      question: "Por que preciso usar um sistema de automação de e-mail?",
      answer: "Um sistema de automação de e-mail ajuda a melhorar a eficiência, alcançar mais clientes e personalizar comunicações para aumentar as taxas de conversão."
    },
    {
      question: "Há uma versão de pré-visualização ou um teste gratuito disponível?",
      answer: "Sim, oferecemos uma versão de teste gratuita para novos usuários explorarem todas as funcionalidades antes de decidir pela compra."
    },
    {
      question: "Quais são as atualizações e melhorias recentes no Conversio?",
      answer: "As atualizações recentes incluem melhorias na interface do usuário, novos templates de e-mail, e otimizações de desempenho para uma experiência mais rápida e confiável."
    },
    {
      question: "Posso usar o Conversio Design System para projetos comerciais?",
      answer: "Sim, o Conversio Design System é adequado para projetos comerciais, oferecendo componentes e diretrizes de design consistentes para aplicativos e websites."
    }
  ];

  return (
    <div className="faq-container">
      <div className="faq-content">
        <div className="faq-header">
          <h2 className="faq-title">Perguntas Frequentes</h2>
          <p className="faq-description">
            Encontre perguntas e respostas relacionadas ao sistema de design, compra, atualizações e suporte.
          </p>
        </div>

        <div>
          {faqData.map((faq, index) => (
            <FaqItem
              key={index}
              question={faq.question}
              answer={faq.answer}
              defaultOpen={index === 2} // Define o terceiro item como aberto por padrão
            />
          ))}
        </div>

        <div className="faq-footer">
          <a 
            href="#" 
            className="see-all-link"
          >
            Ver todas as FAQs
            <span className="see-all-icon">→</span>
          </a>
        </div>

        <div className="faq-contact">
          Contate-nos em <a href="conversio.send@gmail.com" className="contact-email">conversio.send@gmail.com</a> por e-mail
        </div>
      </div>
    </div>
  );
};

export default Faq;
