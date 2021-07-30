package com.dscatalog.controller;

import com.dscatalog.dto.ProductDTO;
import com.dscatalog.service.ProductService;
import com.dscatalog.service.exception.DatabaseException;
import com.dscatalog.service.exception.ResourceNotFoundException;
import com.dscatalog.util.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.dscatalog.mock.MockUtils.createProductDTO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ProductService productService;

    @Autowired
    private TokenUtil tokenUtil;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;
    private ProductDTO productDTO;
    private PageImpl<ProductDTO> productPage;
    private String username;
    private String password;

    @BeforeEach
    public void setup() {
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;
        productDTO = createProductDTO();
        productPage = new PageImpl<>(List.of(productDTO));
        username = "maria@gmail.com";
        password = "123456";

        when(productService.findAllPaged(any(), any(), any())).thenReturn(productPage);
        when(productService.findById(existingId)).thenReturn(productDTO);
        when(productService.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        when(productService.update(eq(existingId), any())).thenReturn(productDTO);
        when(productService.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);

        doNothing().when(productService).delete(existingId);
        doThrow(ResourceNotFoundException.class).when(productService).delete(nonExistingId);
        doThrow(DatabaseException.class).when(productService).delete(dependentId);

        when(productService.save(any())).thenReturn(productDTO);
    }

    @Test
    public void insertShouldReturnProductDTOCreated() throws Exception {
        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
        String jsonBody = mapper.writeValueAsString(productDTO);

        mockMvc.perform(post("/products", existingId)
                .header("Authorization", "Bearer " + accessToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void deleteShouldNoContentWhenIdExists() throws Exception {
        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
        mockMvc.perform(delete("/products/{id}", nonExistingId)
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
        mockMvc.perform(delete("/products/{id}", existingId)
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists() throws Exception {
        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
        String jsonBody = mapper.writeValueAsString(productDTO);

        mockMvc.perform(put("/products/{id}", existingId)
                .header("Authorization", "Bearer " + accessToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
        String jsonBody = mapper.writeValueAsString(productDTO);

        mockMvc.perform(put("/products/{id}", nonExistingId)
                .header("Authorization", "Bearer " + accessToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
        mockMvc.perform(get("/products")
                .param("categoryId", "0")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnProductWhenIdExists() throws Exception {
        mockMvc.perform(get("/products/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        mockMvc.perform(get("/products/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}