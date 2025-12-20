import { useState, useEffect } from 'react';
import { menuService } from '../services/api';
import { MdEdit, MdDelete, MdAdd, MdClose, MdImage } from 'react-icons/md';

const ImageCell = ({ src, alt }) => {
    const [error, setError] = useState(false);

    if (!src || error) {
        return (
            <div style={{
                width: '40px', height: '40px',
                backgroundColor: 'var(--bg-hover)',
                borderRadius: '4px',
                display: 'flex', alignItems: 'center', justifyContent: 'center',
                color: 'var(--text-muted)'
            }}>
                <MdImage size={20} />
            </div>
        );
    }

    return (
        <img
            src={src}
            alt={alt}
            style={{ width: '40px', height: '40px', objectFit: 'cover', borderRadius: '4px', display: 'block' }}
            onError={() => setError(true)}
        />
    );
};

const Menu = () => {
    const [menuItems, setMenuItems] = useState([]);
    const [loading, setLoading] = useState(true);

    // Modal State
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [isEditMode, setIsEditMode] = useState(false);
    const [currentId, setCurrentId] = useState(null);
    const [formData, setFormData] = useState({
        name: '',
        description: '',
        price: 0,
        slug: '',
        image: '',
        availableStock: 0,
        menuCategory: 1 // Default to 1
    });

    useEffect(() => {
        loadMenu();
    }, []);

    const loadMenu = async () => {
        setLoading(true);
        try {
            const response = await menuService.getAll();
            setMenuItems(Array.isArray(response.data) ? response.data : []);
        } catch (error) {
            console.error("Failed to fetch menu items", error);
        } finally {
            setLoading(false);
        }
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => {
            const newData = { ...prev, [name]: value };

            // Auto-generate slug from name if not in edit mode or slug is empty
            if (name === 'name' && !isEditMode) {
                newData.slug = value.toLowerCase()
                    .replace(/[^a-z0-9]+/g, '-')
                    .replace(/(^-|-$)+/g, '');
            }
            return newData;
        });
    };

    const openAddModal = () => {
        setIsEditMode(false);
        setCurrentId(null);
        setFormData({
            name: '',
            description: '',
            price: 0,
            slug: '',
            image: '',
            availableStock: 0,
            menuCategory: 1
        });
        setIsModalOpen(true);
    };

    const openEditModal = (item) => {
        setIsEditMode(true);
        setCurrentId(item.id);
        setFormData({
            name: item.name,
            description: item.description || '',
            price: item.price,
            slug: item.slug,
            image: item.image || '',
            availableStock: item.availableStock,
            menuCategory: item.menuCategory || 1
        });
        setIsModalOpen(true);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const payload = {
                ...formData,
                price: parseFloat(formData.price),
                availableStock: parseInt(formData.availableStock),
                menuCategory: parseInt(formData.menuCategory)
            };

            if (isEditMode) {
                await menuService.update(currentId, payload);
            } else {
                await menuService.create(payload);
            }
            setIsModalOpen(false);
            loadMenu();
        } catch (error) {
            console.error("Failed to save item", error);
            alert("Failed to save item. Please check inputs.");
        }
    };

    const handleDelete = async (id) => {
        if (window.confirm("Are you sure you want to delete this item?")) {
            try {
                await menuService.delete(id);
                loadMenu();
            } catch (error) {
                console.error("Failed to delete item", error);
                alert("Failed to delete item.");
            }
        }
    };

    return (
        <div>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem' }}>
                <h1 className="header-title" style={{ marginBottom: 0 }}>Menu Management</h1>
                <button className="btn btn-primary" onClick={openAddModal}>
                    <MdAdd size={20} />
                    Add Item
                </button>
            </div>

            <div className="card table-container">
                <table>
                    <thead>
                        <tr>
                            <th>Image</th>
                            <th>Name</th>
                            <th>Category ID</th>
                            <th>Price</th>
                            <th>Stock</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {loading ? (
                            <tr><td colSpan="6" style={{ textAlign: 'center' }}>Loading...</td></tr>
                        ) : menuItems.length === 0 ? (
                            <tr><td colSpan="6" style={{ textAlign: 'center' }}>No items found</td></tr>
                        ) : (
                            menuItems.map(item => (
                                <tr key={item.id}>
                                    <td>
                                        <ImageCell src={item.image} alt={item.name} />
                                    </td>
                                    <td>
                                        <div style={{ fontWeight: 500 }}>{item.name}</div>
                                        <div style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>{item.slug}</div>
                                    </td>
                                    <td>{item.menuCategory}</td>
                                    <td>${item.price}</td>
                                    <td>
                                        <span className={`badge ${item.availableStock > 0 ? 'badge-success' : 'badge-warning'}`}>
                                            {item.availableStock || 0}
                                        </span>
                                    </td>
                                    <td>
                                        <div style={{ display: 'flex', gap: '0.5rem' }}>
                                            <button className="btn btn-outline" style={{ padding: '0.4rem' }} onClick={() => openEditModal(item)}>
                                                <MdEdit size={16} />
                                            </button>
                                            <button className="btn btn-danger" style={{ padding: '0.4rem' }} onClick={() => handleDelete(item.id)}>
                                                <MdDelete size={16} />
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            ))
                        )}
                    </tbody>
                </table>
            </div>

            {/* Modal */}
            {isModalOpen && (
                <div className="modal-overlay">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h2 className="modal-title">{isEditMode ? 'Edit Item' : 'Add New Item'}</h2>
                            <button onClick={() => setIsModalOpen(false)} style={{ color: 'var(--text-secondary)' }}>
                                <MdClose size={24} />
                            </button>
                        </div>
                        <form onSubmit={handleSubmit}>
                            <div className="form-group">
                                <label className="form-label">Name</label>
                                <input
                                    type="text"
                                    name="name"
                                    className="input"
                                    value={formData.name}
                                    onChange={handleInputChange}
                                    required
                                />
                            </div>
                            <div className="form-group">
                                <label className="form-label">Slug</label>
                                <input
                                    type="text"
                                    name="slug"
                                    className="input"
                                    value={formData.slug}
                                    onChange={handleInputChange}
                                    required
                                />
                            </div>
                            <div className="form-group">
                                <label className="form-label">Description</label>
                                <textarea
                                    name="description"
                                    className="input"
                                    rows="3"
                                    value={formData.description}
                                    onChange={handleInputChange}
                                />
                            </div>
                            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                                <div className="form-group">
                                    <label className="form-label">Price</label>
                                    <input
                                        type="number"
                                        name="price"
                                        className="input"
                                        min="0"
                                        step="0.01"
                                        value={formData.price}
                                        onChange={handleInputChange}
                                        required
                                    />
                                </div>
                                <div className="form-group">
                                    <label className="form-label">Stock</label>
                                    <input
                                        type="number"
                                        name="availableStock"
                                        className="input"
                                        min="0"
                                        value={formData.availableStock}
                                        onChange={handleInputChange}
                                        required
                                    />
                                </div>
                            </div>
                            <div className="form-group">
                                <label className="form-label">Category ID</label>
                                <input
                                    type="number"
                                    name="menuCategory"
                                    className="input"
                                    value={formData.menuCategory}
                                    onChange={handleInputChange}
                                    required
                                />
                            </div>
                            <div className="form-group">
                                <label className="form-label">Image URL</label>
                                <input
                                    type="text"
                                    name="image"
                                    className="input"
                                    value={formData.image}
                                    onChange={handleInputChange}
                                    placeholder="https://example.com/image.jpg"
                                />
                            </div>
                            <div className="modal-actions">
                                <button type="button" className="btn btn-outline" onClick={() => setIsModalOpen(false)}>Cancel</button>
                                <button type="submit" className="btn btn-primary">{isEditMode ? 'Update' : 'Create'}</button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default Menu;
