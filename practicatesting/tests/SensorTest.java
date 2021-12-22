import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class SensorTest{
    GestorSensores gestor;

    @BeforeEach
    public void init() {
            gestor = new GestorSensores();
            }

    @Test
    public void inicialmenteElNumeroDeSensoresDelGestorEsCero() {
            assertEquals(gestor.getNumeroSensores(), 0);
            }

    @Test
    public void siSeBorraUnSensorNoExistenteSeElevaExcepcion() {
        assertThrows(SensorExcepcion.class, () -> {
            gestor.borrarSensor("NombreInexistente");
        });
    }

    @Test
    public void siSeObtieneLaTemperaturaMediaEnUnGestorVacioSeElevaExcepcion() {
    	assertThrows(SensorExcepcion.class, () -> {
            gestor.getTemperaturaMedia();
        });
    }

    // el numero maximo de sensores permitido es 100
    @Test
    public void siSeIntroduceUnSensorEnUnGestorLlenoSeElevaExcepcion() {

            for (int i = 0; i < GestorSensores.max_sensores(); i++) {
            ISensorTemperatura sensorMock = mock(ISensorTemperatura.class);
            gestor.introducirSensor(sensorMock);
            }
            assertEquals(gestor.getNumeroSensores(), GestorSensores.max_sensores());

            ISensorTemperatura sensorMock = mock(ISensorTemperatura.class);

            assertThrows(SensorExcepcion.class, () -> {
                gestor.introducirSensor(sensorMock);;
            });


            }

    @Test
    public void siSeBorraUnSensorDelGestorSeDecrementaEnUnoElNumeroDeSensores() {
    		ISensorTemperatura sensor1 = mock(ISensorTemperatura.class);
	        ISensorTemperatura sensor2 = mock(ISensorTemperatura.class);
	        ISensorTemperatura sensor3 = mock(ISensorTemperatura.class);
	
	        when(sensor1.getNombre()).thenReturn("sensor1");
	        when(sensor2.getNombre()).thenReturn("sensor2");
	        when(sensor3.getNombre()).thenReturn("sensor3");
	
	        int sensores = 0;
	        gestor.introducirSensor(sensor1);
	        sensores++;
	        gestor.introducirSensor(sensor2);
	        sensores++;
	        gestor.introducirSensor(sensor3);
	        sensores++;
	        
	        assertEquals(gestor.getNumeroSensores(), sensores);
	        
	        assertThrows(SensorExcepcion.class, () -> {
	            gestor.getTemperaturaMedia();
	        });
	
    }

    // se considera temperatura fuera de rango si la temperatura es menor que -90 o
    // mayor que 60
    @Test
    public void siAlgunSensorTieneTemperaturaFueraDeRangoObtenerLaTemperaturaMediaElevaUnaExcepcion() {
    	 ISensorTemperatura sensor1 = mock(ISensorTemperatura.class);
         ISensorTemperatura sensor2 = mock(ISensorTemperatura.class);
         ISensorTemperatura sensor3 = mock(ISensorTemperatura.class);

         when(sensor1.getTemperaturaCelsius()).thenReturn((float)-91);
         when(sensor2.getTemperaturaCelsius()).thenReturn((float)10);
         when(sensor3.getTemperaturaCelsius()).thenReturn((float)65);

         int sensores = 0;
         gestor.introducirSensor(sensor1);
         sensores++;
         gestor.introducirSensor(sensor2);
         sensores++;
         gestor.introducirSensor(sensor3);
         sensores++;

         assertEquals(gestor.getNumeroSensores(), sensores);

         assertThrows(SensorExcepcion.class, () -> {
	            gestor.getTemperaturaMedia();
         });
    }

    // prueba con tres valores a tu eleccion
    @Test
    public void laTemperaturaMediaDeTresSensoresObtenidaATravesDelGestorEsCorrecta() {
    	 ISensorTemperatura sensor1 = mock(ISensorTemperatura.class);
         ISensorTemperatura sensor2 = mock(ISensorTemperatura.class);
         ISensorTemperatura sensor3 = mock(ISensorTemperatura.class);

         when(sensor1.getTemperaturaCelsius()).thenReturn((float)30);
         when(sensor2.getTemperaturaCelsius()).thenReturn((float)10);
         when(sensor3.getTemperaturaCelsius()).thenReturn((float)20);

         when(sensor1.disponible()).thenReturn(true);
         when(sensor2.disponible()).thenReturn(true);
         when(sensor3.disponible()).thenReturn(true);
         
         gestor.introducirSensor(sensor1);
         gestor.introducirSensor(sensor2);
         gestor.introducirSensor(sensor3);
         
         assertEquals(gestor.getTemperaturaMedia(), 20);     
    }

    @Test
    public void siSeContactaTresVecesConSensoresDisponiblesNoSeBorraNinguno() {
            ISensorTemperatura sensor1 = mock(ISensorTemperatura.class);
            ISensorTemperatura sensor2 = mock(ISensorTemperatura.class);
            ISensorTemperatura sensor3 = mock(ISensorTemperatura.class);

            when(sensor1.disponible()).thenReturn(true);
            when(sensor2.disponible()).thenReturn(true);
            when(sensor3.disponible()).thenReturn(true);

            int sensores = 0;
            gestor.introducirSensor(sensor1);
            sensores++;
            gestor.introducirSensor(sensor2);
            sensores++;
            gestor.introducirSensor(sensor3);
            sensores++;

            assertEquals(gestor.getNumeroSensores(), sensores);

            gestor.contactarSensores();
            gestor.contactarSensores();
            gestor.contactarSensores();

            assertEquals(gestor.getNumeroSensores(), sensores);
        }

    @Test
    public void siSeContactaTresVecesConUnSensorNoDisponibleSeBorraDelGestor() {
    	ISensorTemperatura sensor1 = mock(ISensorTemperatura.class);
        ISensorTemperatura sensor2 = mock(ISensorTemperatura.class);
        ISensorTemperatura sensor3 = mock(ISensorTemperatura.class);

        when(sensor1.disponible()).thenReturn(true);
        when(sensor2.disponible()).thenReturn(false);
        when(sensor3.disponible()).thenReturn(true);

        int sensores = 0;
        gestor.introducirSensor(sensor1);
        sensores++;
        gestor.introducirSensor(sensor2);
        sensores++;
        gestor.introducirSensor(sensor3);
        sensores++;

        assertEquals(gestor.getNumeroSensores(), sensores);

        gestor.contactarSensores();
        gestor.contactarSensores();
        gestor.contactarSensores();

        assertEquals(gestor.getNumeroSensores(), sensores-1);
    }
}