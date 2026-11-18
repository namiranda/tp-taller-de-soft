import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';
import App from './App';
import { SocioProvider } from './contexto/SocioContext';
import './estilos/global.css';

const raiz = ReactDOM.createRoot(document.getElementById('root'));
raiz.render(
  <React.StrictMode>
    <BrowserRouter>
      <SocioProvider>
        <App />
      </SocioProvider>
    </BrowserRouter>
  </React.StrictMode>
);
