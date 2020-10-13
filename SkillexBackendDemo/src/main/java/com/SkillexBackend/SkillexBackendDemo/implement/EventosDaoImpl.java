package com.SkillexBackend.SkillexBackendDemo.implement;

import com.SkillexBackend.SkillexBackendDemo.dao.EventosDao;
import com.SkillexBackend.SkillexBackendDemo.models.Eventos;
import com.SkillexBackend.SkillexBackendDemo.repository.EventosRepository;
import com.SkillexBackend.SkillexBackendDemo.vo.EventosVO;
import com.SkillexBackend.SkillexBackendDemo.vo.RespuestaOperaciones;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventosDaoImpl implements EventosDao {

    private EntityManagerFactory emf;

    @Autowired
    private EventosRepository eventosRepository;

//SERVICIO LISTAR EVENTOS
    @Override
    public List<EventosVO> mostrar_eventos() {

        EventosVO even = new EventosVO();
        List<EventosVO> responseEventos = new ArrayList<EventosVO>();

        emf = Persistence.createEntityManagerFactory("com.miUnidadDePersistencia");

        try {
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();
            String sql = "SELECT * FROM eventos e "
                    + "JOIN detalle_evento de ON e.idEventos = de.id_detalle_evento ";

            Query query = em.createNativeQuery(sql);
            List<Object[]> listOb = query.getResultList();

            if (listOb.size() > 0) {

                Iterator it = listOb.iterator();
                while (it.hasNext()) {
                    Object[] line = (Object[]) it.next();
                    Integer idEventos = (Integer) line[0];
                    String nombre_evento = (String) line[1];
                    String autor_evento = (String) line[2];
                    Integer Usuario_idUsuarios = (Integer) line[3];
                    Integer detalle_evento_id_detalle_evento = (Integer) line[4];
                    Integer id_detalle_evento = (Integer) line[5];
                    String tipo_evento = (String) line[6];
                    String servicio_ofrecido = (String) line[7];
                    String imagen_evento = (String) line[8];

                    even = new EventosVO(idEventos, nombre_evento, autor_evento, Usuario_idUsuarios, detalle_evento_id_detalle_evento, id_detalle_evento, tipo_evento, servicio_ofrecido, imagen_evento);
                    responseEventos.add(even);
                    even = new EventosVO();
                }
                return responseEventos;
            } else {
                return responseEventos;
            }

        } catch (Exception e) {
            System.out.println(e.toString());
            return responseEventos;
        }
    }

    @Override
    public Page<Eventos> findAll(Pageable pageable) {
        return null;
    }

// SERVICIO ACTUALIZAR EVENTOS
    @Override
    public Object updateEvento(EventosVO evento) {
        EventosVO eveUp = evento;
        RespuestaOperaciones resp = new RespuestaOperaciones();

        if (eveUp.getIdEventos() != null) {
            emf = Persistence.createEntityManagerFactory("com.miUnidadDePersistencia");
            EntityManager emd = emf.createEntityManager();
            emd.getTransaction().begin();
            try {
                //Update a eventos
                String sql = "update eventos "
                        + "set nombre_evento = :nombre, "
                        + "autor_evento = :autor "
                        + "where idEventos = :idEven";
                Query query = emd.createNativeQuery(sql);

                query.setParameter("nombre", eveUp.getNombre_evento());
                query.setParameter("autor", eveUp.getAutor_evento());
                query.setParameter("idEven", eveUp.getIdEventos());
                query.executeUpdate();
                emd.getTransaction().commit();

                //Update a detalles de eventos
                EntityManager emd2 = emf.createEntityManager();
                emd2.getTransaction().begin();

                String sql2 = "Update detalle_evento "
                        + "set tipo_evento = :tipo, "
                        + "servicio_ofrecido = :servicio, "
                        + "imagen_evento= :imagen "
                        + "where  id_detalle_evento = :evenId";
                Query query2 = emd2.createNativeQuery(sql2);
                query2.setParameter("tipo", eveUp.getTipo_evento());
                query2.setParameter("servicio", eveUp.getServicio_ofrecido());
                query2.setParameter("imagen", eveUp.getImagen_evento());
                query2.setParameter("evenId", eveUp.getId_detalle_evento());

                query2.executeUpdate();
                emd2.getTransaction().commit();
                emd2.close();

                resp.setCodigo("001");
                resp.setRespuesta("Evento actualizado");
                return resp;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }

        } else {
            return null;
        }
    }

    @Override
    @Transactional
//SERVICIO CREAR EVENTOS
    public Object save(EventosVO evento) {
        
        Integer idEventos = 0;
        
        emf = Persistence.createEntityManagerFactory("com.miUnidadDePersistencia");
        RespuestaOperaciones resp = new RespuestaOperaciones();
        EntityManager emd = emf.createEntityManager();
        emd.getTransaction().begin();

        if (idEventos == 0) {
            try {
                //insert a detalle evento
                EntityManager emD1 = emf.createEntityManager();
                emD1.getTransaction().begin();
                String sql3 = "insert into detalle_evento(tipo_evento,servicio_ofrecido,imagen_evento)"
                        + "values(:tipo_evento, :servicio_ofrecido, :imagen_evento)";
                Query query2 = emD1.createNativeQuery(sql3);
                query2.setParameter("tipo_evento", evento.getTipo_evento());
                query2.setParameter("servicio_ofrecido", evento.getServicio_ofrecido());
                query2.setParameter("imagen_evento", evento.getImagen_evento());
                
                query2.executeUpdate();
                emD1.getTransaction().commit();

                // buscar detalle_evento Creado
                EntityManager emD2 = emf.createEntityManager();
                emD2.getTransaction().begin();
                String sql4 = "SELECT id_detalle_evento,tipo_evento FROM skillexbd.detalle_evento ORDER BY id_detalle_evento desc limit 1";
                Query query3 = emD2.createNativeQuery(sql4);
                List<Object[]> listP1 = query3.getResultList();
                Iterator it2 = listP1.iterator();
                
                while (it2.hasNext()) {
                    Object[] line = (Object[]) it2.next();
                    idEventos = (Integer) line[0];
                    String tipe_event = (String) line[1];    
                }
                // insert a evento
                EntityManager emt = emf.createEntityManager();
                emt.getTransaction().begin();
                String sql = "insert into eventos(nombre_evento, autor_evento, Usuario_idUsuarios, detalle_evento_id_detalle_evento)"
                        + "values(:nombre_evento, :autor_evento, :Usuario_idUsuarios, :detalle_evento_id_detalle_evento)";
                Query query = emt.createNativeQuery(sql);
                query.setParameter("nombre_evento", evento.getNombre_evento());
                query.setParameter("autor_evento", evento.getAutor_evento());
                query.setParameter("Usuario_idUsuarios", evento.getUsuario_idUsuarios());
                query.setParameter("detalle_evento_id_detalle_evento", idEventos);
                
                query.executeUpdate();
                emt.getTransaction().commit();
                emt.close();

                resp.setCodigo("001");
                resp.setRespuesta("Evento creado");
                return resp;
            } catch (Exception e) {
                System.err.println(e.toString());
                resp.setCodigo("002");
                resp.setRespuesta("Error al crear un evento");
                return resp;
            }
        } else {
            resp.setCodigo("003");
            resp.setRespuesta("Error, el evento ya existe");
            return resp;
        }

    }

//SERVICIO ELIMINAR EVENTOS    
    @Override
    public Object deleteById(EventosVO borrar) {
        emf = Persistence.createEntityManagerFactory("com.miUnidadDePersistencia");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        RespuestaOperaciones resp = new RespuestaOperaciones();
        try {
            
            EntityManager emD1 = emf.createEntityManager();
            emD1.getTransaction().begin();
           
            String sql2 = "delete From eventos where idEventos = :id";
            Query query = em.createNativeQuery(sql2);
            query.setParameter("id", borrar.getIdEventos());
            
            query.executeUpdate();
            em.getTransaction().commit();
            
            String sql3 = "delete from detalle_evento where id_detalle_evento = :id";
            Query query2 = emD1.createNativeQuery(sql3);
            query2.setParameter("id", borrar.getId_detalle_evento());
    
            query2.executeUpdate();                
            emD1.getTransaction().commit();
            em.close();
           
            resp.setCodigo("001");
            resp.setRespuesta("OK");
            return resp;
        } catch (Exception e) {
            System.out.println(e.toString());
            resp.setCodigo("002");
            resp.setRespuesta("Error al eliminar el evento");
            return resp;
        }
    }

}