import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Cookies from 'js-cookie';
import '../home/Dashboard.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faEnvelope, faEnvelopeOpenText, faUsersLine, faEnvelopeCircleCheck, faFilter, faArrowRight, faChartSimple } from '@fortawesome/free-solid-svg-icons';
import Header from '../../components/header/Header';
import Logs from '../../components/logs/Logs';
import banner from "../../assets/images/banner-dashboard.jpg";
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import Faq from '../../components/footer/Faq';
import EmailAutomationIcons from '../../components/home/EmailAutomationIcons';


function Dashboard() {
    const [dashboardData, setDashboardData] = useState({
        quantityEmailsIn24h: 0,
        totalEmailsSended: 0,
        totalClients: 0,
        fullEmailCapicity: 0
    });
    const [progressEmailSentToday, setProgressEmailSentToday] = useState(0);
    const [progressTotalEmailsSended, setProgressTotalEmailsSended] = useState(0);
    const [progressTotalClients, setProgressTotalClients] = useState(0);
    const [progressFullEmailCapicity, setProgressFullEmailCapicity] = useState(0);
    
    const data = [
        {
            name: 'Emails Enviados nas Últimas 24h',
            Valor: dashboardData.quantityEmailsIn24h,
        },
        {
            name: 'Total de Emails Enviados',
            Valor: progressTotalEmailsSended,
        },
        {
            name: 'Total de Clientes',
            Valor: progressTotalClients,
        },
        {
            name: 'Capacidade Total de Envios',
            Valor: progressFullEmailCapicity,
        },
    ];
    

    const [error, setError] = useState<string | null>(null);

    const fetchDashboardData = async () => {
        const token = Cookies.get("accessToken");

        if (!token) {
            setError("Usuário não autenticado. Faça login novamente.");
            return;
        }

        try {
            const response = await axios.get(
                'http://localhost:8080/api/v1/dashboard/get/datas',
                { headers: { Authorization: `Bearer ${token}` } }
            );

            setDashboardData({
                quantityEmailsIn24h: response.data.quantityEmailsIn24h ?? 0,
                totalEmailsSended: response.data.totalEmailsSended ?? 0,
                totalClients: response.data.totalClients ?? 0,
                fullEmailCapicity: response.data.fullEmailCapicity ?? 0
            });
        } catch (err) {
            setError("Falha ao buscar dados do dashboard: " + (err.response?.data?.message || err.message));
        }
    };

    useEffect(() => {
        fetchDashboardData();
    }, []);

    useEffect(() => {
        // Função para aumentar a barra de progresso gradualmente
        const intervalTime = 10; // Intervalo em ms para cada aumento
        const maxProgressEmailSentToday = (dashboardData.quantityEmailsIn24h / 100) * 100;
        const maxProgressTotalEmailsSended = (dashboardData.totalEmailsSended / 100) * 100;
        const maxProgressTotalClients = (dashboardData.totalClients / 100) * 100;
        const maxProgressFullEmailCapicity = (dashboardData.fullEmailCapicity / 100) * 100;

        // Email Sent Today
        const progressInterval = setInterval(() => {
            setProgressEmailSentToday((prev) => {
                if (prev >= maxProgressEmailSentToday) {
                    clearInterval(progressInterval);  // Limpeza após alcançar o valor máximo
                    return maxProgressEmailSentToday;
                }
                return prev + 1;
            });
        }, intervalTime);

        // Total Emails Sent
        const progressInterval2 = setInterval(() => {
            setProgressTotalEmailsSended((prev) => {
                if (prev >= maxProgressTotalEmailsSended) {
                    clearInterval(progressInterval2);  // Limpeza após alcançar o valor máximo
                    return maxProgressTotalEmailsSended;
                }
                return prev + 1;
            });
        }, intervalTime);

        // Total Clients
        const progressInterval3 = setInterval(() => {
            setProgressTotalClients((prev) => {
                if (prev >= maxProgressTotalClients) {
                    clearInterval(progressInterval3);  // Limpeza após alcançar o valor máximo
                    return maxProgressTotalClients;
                }
                return prev + 1;
            });
        }, intervalTime);

        // Full Email Capacity
        const progressInterval4 = setInterval(() => {
            setProgressFullEmailCapicity((prev) => {
                if (prev >= maxProgressFullEmailCapicity) {
                    clearInterval(progressInterval4);  // Limpeza após alcançar o valor máximo
                    return maxProgressFullEmailCapicity;
                }
                return prev + 20;
            });
        }, intervalTime);

        // Limpeza dos intervalos ao desmontar o componente
        return () => {
            clearInterval(progressInterval);
            clearInterval(progressInterval2);
            clearInterval(progressInterval3);
            clearInterval(progressInterval4);
        };

    }, [dashboardData]);


    return (
        <div style={{ width: "100%", height: "auto", paddingBottom: "50px" }}>
            <div className='container-principal'>
                <div className='header-button-component'><Header /></div>
                <div className="card" style={{ height: "180px" }}>
                    <div className="title">
                        <span>
                            <FontAwesomeIcon icon={faEnvelope} className='icons-header' />
                        </span>
                        <p className="title-text">Emails Enviados nas últimas 24h</p>
                    </div>
                    <div className="data">
                        <p>{dashboardData.quantityEmailsIn24h}</p>
                        <div className="range">
                            <div className="fill" style={{ width: `${((progressEmailSentToday) / 5000) * 100}%` }}></div>
                        </div>
                    </div>
                </div>

                <div className="card" style={{ height: "180px" }}>
                    <div className="title">
                        <span>
                            <FontAwesomeIcon icon={faEnvelopeOpenText} className='icons-header' />
                        </span>
                        <p className="title-text">Total de Emails Enviados</p>
                    </div>
                    <div className="data">
                        <p>{progressTotalEmailsSended.toFixed(0)}</p>
                        <div className="range">
                            <div className="fill" style={{ width: `${((progressTotalEmailsSended) / 5000) * 100}%` }}></div>
                        </div>
                    </div>
                </div>

                <div className="card" style={{ height: "180px" }}>
                    <div className="title">
                        <span>
                            <FontAwesomeIcon icon={faUsersLine} className='icons-header' />
                        </span>
                        <p className="title-text">Total de Clientes</p>
                    </div>
                    <div className="data">
                        <p>{progressTotalClients}/100</p>
                        <div className="range">
                            <div className="fill" style={{ width: `${progressTotalClients}%` }}></div>
                        </div>
                    </div>
                </div>

                <div className="card" style={{ height: "180px" }}>
                    <div className="title">
                        <span>
                            <FontAwesomeIcon icon={faEnvelopeCircleCheck} className='icons-header' />
                        </span>
                        <p className="title-text">Capacidade Total de Envios</p>
                    </div>
                    <div className="data">
                        <p>{progressFullEmailCapicity}/5000</p>
                        <div className="range">
                            <div className="fill"
                                style={{ width: `${((progressFullEmailCapicity) / 5000) * 100}%` }}>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
            <div className='line-separator'></div><br />
            {/* Adicionando o gráfico Recharts */}
            <div className="graphic" style={{ height: "400px" }}>
                <div className="title">
                    <FontAwesomeIcon icon={faChartSimple} className='icons-header' />
                    <p className="title-text">Gráfico de Progresso</p>
                </div>
                <ResponsiveContainer width="80%" height="100%">
                    <BarChart data={data}>
                        <CartesianGrid strokeDasharray="3 3" />
                        <XAxis dataKey="name" />
                        <YAxis />
                        <Tooltip />
                        <Legend />
                        <Bar dataKey="Valor" fill="#8884d8"  radius={[5, 5, 5, 5]} style={{cursor: "pointer"}}/>
                    </BarChart>
                </ResponsiveContainer>
            </div>
            <EmailAutomationIcons />
            <Faq />
        </div>
    );
}

export default Dashboard;