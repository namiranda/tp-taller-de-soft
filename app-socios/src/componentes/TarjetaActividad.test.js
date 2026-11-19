import { fireEvent, render, screen } from '@testing-library/react';
import '@testing-library/jest-dom';
import { MemoryRouter } from 'react-router-dom';
import TarjetaActividad from './TarjetaActividad';

const actividad = {
  id: 1, nombre: 'Natación', disciplina: 'Natación', profesor: 'Sofía',
  dias: 'Martes', horario: '17:00:00', edadMinima: 5, edadMaxima: 70,
  cupoMaximo: 30, cupoDisponible: 1, cuotaMensual: 26000,
};

test('muestra el nombre y permite solicitar la inscripción', () => {
  const inscribir = jest.fn();
  render(<MemoryRouter><TarjetaActividad actividad={actividad} onInscribirme={inscribir} /></MemoryRouter>);

  expect(screen.getByRole('heading', { name: 'Natación' })).toBeInTheDocument();
  fireEvent.click(screen.getByRole('button', { name: 'Inscribirme' }));
  expect(inscribir).toHaveBeenCalledWith(actividad);
});

test('sin cupo muestra el aviso y deshabilita la inscripción', () => {
  const inscribir = jest.fn();
  render(<MemoryRouter><TarjetaActividad actividad={{ ...actividad, cupoDisponible: 0 }} onInscribirme={inscribir} /></MemoryRouter>);

  expect(screen.getByText('Sin cupo')).toBeInTheDocument();
  const boton = screen.getByRole('button', { name: 'Inscribirme' });
  expect(boton).toBeDisabled();
  fireEvent.click(boton);
  expect(inscribir).not.toHaveBeenCalled();
});
